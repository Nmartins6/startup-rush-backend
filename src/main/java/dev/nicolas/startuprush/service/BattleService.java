package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.*;
import dev.nicolas.startuprush.model.BattleEvent;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.repository.BattleEventRepository;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BattleService {

    private final BattleEventRepository battleEventRepository;

    public BattleService(StartupRepository startupRepository,
                         StartupBattleRepository battleRepository, BattleEventRepository battleEventRepository) {
        this.startupRepository = startupRepository;
        this.battleRepository = battleRepository;
        this.battleEventRepository = battleEventRepository;
    }

    private final StartupBattleRepository battleRepository;
    private final StartupRepository startupRepository;
    private final Random random = new Random();

    @Transactional
    public StartupBattle applyBattleEvents(BattleEventsRequestDTO request) {
        StartupBattle battle = battleRepository.findById(request.getBattleId())
                .orElseThrow(() -> new RuntimeException("Battle not found"));

        Startup startupA = battle.getStartupA();
        Startup startupB = battle.getStartupB();

        int updatedScoreA = applyEventsToStartup(startupA, request.getEventsForStartupA(), battle);
        int updatedScoreB = applyEventsToStartup(startupB, request.getEventsForStartupB(), battle);

        startupA.setScore(updatedScoreA);
        startupB.setScore(updatedScoreB);

        if (updatedScoreA > updatedScoreB) {
            battle.setWinner(startupA);
            startupA.setScore(startupA.getScore() + 30);
        } else if (updatedScoreB > updatedScoreA) {
            battle.setWinner(startupB);
            startupB.setScore(startupB.getScore() + 30);
        } else {
            Startup winner = new Random().nextBoolean() ? startupA : startupB;
            winner.setScore(winner.getScore() + 2);

            BattleEvent sharkFightEvent = BattleEvent.builder()
                    .type("SHARK_FIGHT")
                    .points(2)
                    .startup(winner)
                    .battle(battle)
                    .build();
            battleEventRepository.save(sharkFightEvent);

            winner.setScore(winner.getScore() + 30);
            battle.setWinner(winner);
        }

        battle.setCompleted(true);

        startupRepository.save(startupA);
        startupRepository.save(startupB);
        battleRepository.save(battle);

        int currentRound = battle.getRound();
        boolean allCompleted = battleRepository.findAll().stream()
                .filter(b -> b.getRound() == currentRound)
                .allMatch(StartupBattle::isCompleted);

        if (allCompleted) {
            startNextRound();
        }

        return battle;
    }

    private int applyEventsToStartup(Startup startup, List<BattleEventDTO> events, StartupBattle battle) {
        if (events == null || events.isEmpty()) return startup.getScore();

        int totalPoints = 0;

        for (BattleEventDTO dto : events) {
            boolean alreadyExists = battleEventRepository.existsByStartupIdAndBattleIdAndType(
                    startup.getId(),
                    battle.getId(),
                    dto.getType()
            );

            if (alreadyExists) {
                throw new IllegalArgumentException("Startup '" + startup.getName() + "' has already received the event '" + dto.getType() + "' in this battle.");
            }

            BattleEvent event = BattleEvent.builder()
                    .type(dto.getType())
                    .points(dto.getPoints())
                    .startup(startup)
                    .battle(battle)
                    .build();

            totalPoints += dto.getPoints();
            battleEventRepository.save(event);
        }

        return startup.getScore() + totalPoints;
    }

    public StartupBattle createRandomBattle() {
        int currentRound = battleRepository.findMaxRound().orElse(0);

        List<StartupBattle> existingBattles = battleRepository.findByRound(currentRound);

        Set<Long> usedStartupIds = existingBattles.stream()
                .flatMap(b -> Stream.of(b.getStartupA().getId(), b.getStartupB().getId()))
                .collect(Collectors.toSet());

        List<Startup> availableStartups = startupRepository.findAll().stream()
                .filter(s -> !usedStartupIds.contains(s.getId()))
                .collect(Collectors.toList());

        if (availableStartups.size() < 2) {
            throw new IllegalStateException("Not enough available startups to create a new battle.");
        }

        Collections.shuffle(availableStartups);

        StartupBattle battle = StartupBattle.builder()
                .startupA(availableStartups.get(0))
                .startupB(availableStartups.get(1))
                .round(currentRound)
                .completed(false)
                .build();

        return battleRepository.save(battle);
    }

    public List<StartupBattle> getPendingBattles() {
        return battleRepository.findByCompletedFalse();
    }

    @Transactional
    public List<StartupBattle> startNextRound() {
        List<StartupBattle> allBattles = battleRepository.findAll();

        if (allBattles.isEmpty()) {
            throw new IllegalStateException("No battles found.");
        }

        int lastRound = allBattles.stream()
                .mapToInt(StartupBattle::getRound)
                .max()
                .orElse(0);

        boolean hasPending = allBattles.stream()
                .anyMatch(b -> b.getRound() == lastRound && !b.isCompleted());

        if (hasPending) {
            throw new IllegalStateException("There are still pending battles.");
        }

        List<Startup> winners = new ArrayList<>(allBattles.stream()
                .filter(b -> b.getRound() == lastRound && b.getWinner() != null)
                .map(StartupBattle::getWinner)
                .toList());

        if (winners.size() == 1) {
            return Collections.emptyList();
        }

        if (winners.size() < 2 || winners.size() % 2 != 0) {
            throw new IllegalStateException("You need an even number of winners (at least 2) to create the next round.");
        }

        Collections.shuffle(winners);
        int newRound = lastRound + 1;

        List<StartupBattle> newBattles = new ArrayList<>();

        for (int i = 0; i < winners.size(); i += 2) {
            StartupBattle newBattle = StartupBattle.builder()
                    .startupA(winners.get(i))
                    .startupB(winners.get(i + 1))
                    .round(newRound)
                    .completed(false)
                    .build();

            newBattles.add(battleRepository.save(newBattle));
        }

        return newBattles;
    }

    public List<RoundReportDTO> generateRoundReport() {
        List<RoundReportDTO> roundReports = new ArrayList<>();

        List<Integer> rounds = battleRepository.findAll().stream()
                .map(StartupBattle::getRound)
                .distinct()
                .sorted()
                .toList();

        for (Integer round : rounds) {
            List<StartupBattle> battles = battleRepository.findByRound(round);
            List<BattleReportDTO> battleReports = new ArrayList<>();

            for (StartupBattle battle : battles) {
                List<BattleEvent> eventsA = battleEventRepository.findByStartupId(battle.getStartupA().getId())
                        .stream()
                        .filter(e -> e.getBattle().getId().equals(battle.getId()))
                        .toList();

                List<BattleEvent> eventsB = battleEventRepository.findByStartupId(battle.getStartupB().getId())
                        .stream()
                        .filter(e -> e.getBattle().getId().equals(battle.getId()))
                        .toList();

                BattleReportDTO battleDTO = BattleReportDTO.builder()
                        .battleId(battle.getId())
                        .startupA(battle.getStartupA().getName())
                        .startupB(battle.getStartupB().getName())
                        .winner(battle.getWinner() != null ? battle.getWinner().getName() : null)
                        .eventsA(eventsA.stream()
                                .map(e -> BattleEventReportDTO.builder()
                                        .type(e.getType())
                                        .points(e.getPoints())
                                        .build())
                                .toList())
                        .eventsB(eventsB.stream()
                                .map(e -> BattleEventReportDTO.builder()
                                        .type(e.getType())
                                        .points(e.getPoints())
                                        .build())
                                .toList())
                        .build();

                battleReports.add(battleDTO);
            }

            roundReports.add(RoundReportDTO.builder()
                    .round(round)
                    .battles(battleReports)
                    .build());
        }

        return roundReports;
    }

    public ChampionDTO getChampion() {
        List<StartupBattle> allBattles = battleRepository.findAll();

        boolean hasPending = allBattles.stream().anyMatch(b -> !b.isCompleted());
        if (hasPending) {
            throw new IllegalStateException("There are still pending battles.");
        }

        int lastRound = allBattles.stream()
                .mapToInt(StartupBattle::getRound)
                .max()
                .orElse(0);

        List<Startup> finalists = allBattles.stream()
                .filter(b -> b.getRound() == lastRound)
                .map(StartupBattle::getWinner)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (finalists.size() != 1) {
            throw new IllegalStateException("The tournament is still ongoing.");
        }

        Startup champion = finalists.get(0);

        return ChampionDTO.builder()
                .name(champion.getName())
                .slogan(champion.getSlogan())
                .build();
    }

    @Transactional
    public List<StartupBattle> startTournament() {
        List<Startup> startups = startupRepository.findAll();
        long battleCount = battleRepository.count();

        if (battleCount > 0) {
            throw new IllegalStateException("Tournament already started");
        }

        if (startups.size() < 4 || startups.size() > 8 || startups.size() % 2 != 0) {
            throw new IllegalStateException("Number of startups must be between 4 and 8, and even.");
        }

        Collections.shuffle(startups);

        List<StartupBattle> battles = new ArrayList<>();
        int round = 0;

        for (int i = 0; i < startups.size(); i += 2) {
            StartupBattle battle = StartupBattle.builder()
                    .startupA(startups.get(i))
                    .startupB(startups.get(i + 1))
                    .round(round)
                    .completed(false)
                    .build();
            battles.add(battleRepository.save(battle));
        }

        return battles;
    }

}
