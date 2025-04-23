package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.startup.*;
import dev.nicolas.startuprush.model.BattleEvent;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.repository.BattleEventRepository;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StartupService {
    private final StartupRepository startupRepository;
    private final StartupBattleRepository battleRepository;
    private final BattleEventRepository battleEventRepository;

    public StartupService(StartupRepository startupRepository, StartupBattleRepository battleRepository, BattleEventRepository battleEventRepository) {
        this.startupRepository = startupRepository;
        this.battleRepository = battleRepository;
        this.battleEventRepository = battleEventRepository;
    }

    public Startup registerStartup(StartupDTO dto) {
        long count = startupRepository.count();
        if (count >= 8) {
            throw new IllegalStateException("Cannot register more than 8 startups");
        }

        if (startupRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("A startup with this name already exists.");
        }

        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Startup name cannot be empty.");
        }

        if (dto.getSlogan() == null || dto.getSlogan().trim().isEmpty()) {
            throw new IllegalArgumentException("Startup slogan cannot be empty.");
        }

        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        if (dto.getFoundationYear() < 1800 || dto.getFoundationYear() > currentYear) {
            throw new IllegalArgumentException("Invalid foundation year. It must be between 1800 and " + currentYear + ".");
        }

        Startup startup = Startup.builder()
                .name(dto.getName())
                .slogan(dto.getSlogan())
                .foundationYear(dto.getFoundationYear())
                .score(70)
                .build();

        return startupRepository.save(startup);
    }

    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }

    @Transactional
    public void clearAllStartups() {
        if (battleRepository.count() > 0) {
            throw new IllegalStateException("Cannot clear startups after the tournament has started.");
        }

        startupRepository.deleteAll();
    }

    public List<Startup> drawTwoRandomStartups() {
        List<Startup> all = startupRepository.findAll();

        if(all.size() < 2) {
            throw new IllegalStateException("At least two startups are required to start a battle");
        }

        Collections.shuffle(all);
        return all.subList(0,2);
    }

    public List<StartupReportDTO> getRankingReport() {
        List<Startup> startups = startupRepository.findAll();
        List<BattleEvent> allEvents = battleEventRepository.findAll();
        List<StartupBattle> allBattles = battleRepository.findAll();

        return startups.stream()
                .map(startup -> {
                    List<BattleEvent> events = allEvents.stream()
                            .filter(e -> Objects.equals(e.getStartup().getId(), startup.getId()))
                            .toList();

                    boolean advanceByBye = allBattles.stream()
                            .anyMatch(b -> b.getWinner() != null
                                    && b.getWinner().getId() == (startup.getId())
                                    && b.isAdvanceByBye());

                    long pitchCount = events.stream().filter(e -> e.getType().equals("PITCH")).count();
                    long bugsCount = events.stream().filter(e -> e.getType().equals("BUG")).count();
                    long userTractionCount = events.stream().filter(e -> e.getType().equals("USER_TRACTION")).count();
                    long investorAngerCount = events.stream().filter(e -> e.getType().equals("INVESTOR_ANGER")).count();
                    long fakeNewsCount = events.stream().filter(e -> e.getType().equals("FAKE_NEWS")).count();
                    long sharkFightCount = events.stream().filter(e -> e.getType().equals("SHARK_FIGHT")).count();

                    return StartupReportDTO.builder()
                            .id(startup.getId())
                            .name(startup.getName())
                            .slogan(startup.getSlogan())
                            .score(startup.getScore())
                            .pitchCount((int) pitchCount)
                            .bugsCount((int) bugsCount)
                            .userTractionCount((int) userTractionCount)
                            .investorAngerCount((int) investorAngerCount)
                            .fakeNewsCount((int) fakeNewsCount)
                            .sharkFightCount((int) sharkFightCount)
                            .advanceByBye(advanceByBye)
                            .build();
                })
                .sorted(Comparator.comparingInt(StartupReportDTO::getScore).reversed())
                .toList();
    }

    public List<StartupRankingDTO> getCompactRanking() {
        return startupRepository.findAll().stream()
                .map(s -> new StartupRankingDTO(s.getId(), s.getName(), s.getScore()))
                .sorted(Comparator.comparingInt(StartupRankingDTO::getScore).reversed())
                .toList();
    }

    public StartupHistoryDTO getStartupHistory(Long startupId) {
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new IllegalArgumentException("Startup not found"));

        List<StartupBattle> battles = battleRepository.findAll().stream()
                .filter(b -> Objects.equals(b.getStartupA().getId(), startupId) ||
                        (b.getStartupB() != null && Objects.equals(b.getStartupB().getId(), startupId)))
                .toList();

        List<BattleEvent> allBattleEvents = battleEventRepository.findByStartupId(startupId);

        List<StartupBattleHistoryDTO> battleHistoryList = new ArrayList<>();

        for (StartupBattle battle : battles) {
            boolean isStartupA = Objects.equals(battle.getStartupA().getId(), startupId);
            Startup opponent = isStartupA ? battle.getStartupB() : battle.getStartupA();

            String opponentName = opponent != null ? opponent.getName() : "BYE";

            String result = "PENDING";
            if (battle.isCompleted()) {
                if (battle.getWinner() != null && Objects.equals(battle.getWinner().getId(), startupId)) {
                    result = "WIN";
                } else {
                    result = "LOSS";
                }
            }

            List<StartupHistoryEventDTO> events = allBattleEvents.stream()
                    .filter(e -> Objects.equals(e.getBattle().getId(), battle.getId()))
                    .map(e -> StartupHistoryEventDTO.builder()
                            .type(e.getType())
                            .points(e.getPoints())
                            .build())
                    .toList();

            battleHistoryList.add(StartupBattleHistoryDTO.builder()
                    .round(battle.getRound())
                    .opponent(opponentName)
                    .result(result)
                    .events(events)
                    .build());
        }

        return StartupHistoryDTO.builder()
                .startup(startup.getName())
                .battles(battleHistoryList)
                .build();
    }

    @Transactional
    public Startup updateStartup(Long id, UpdateStartupDTO dto) {
        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Startup not found"));

        if (dto.getName() != null) {
            String trimmedName = dto.getName().trim();
            if (trimmedName.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }
            if (!startup.getName().equalsIgnoreCase(trimmedName) && startupRepository.existsByName(trimmedName)) {
                throw new IllegalArgumentException("A startup with this name already exists.");
            }
            startup.setName(trimmedName);
        }

        if (dto.getSlogan() != null) {
            String trimmedSlogan = dto.getSlogan().trim();
            if (trimmedSlogan.isEmpty()) {
                throw new IllegalArgumentException("Slogan cannot be empty.");
            }
            startup.setSlogan(trimmedSlogan);
        }

        if (dto.getFoundationYear() != null) {
            int year = dto.getFoundationYear();
            if (year < 1800 || year > 2025) {
                throw new IllegalArgumentException("Invalid foundation year. It must be between 1800 and 2025.");
            }
            startup.setFoundationYear(year);
        }

        return startupRepository.save(startup);
    }

    @Transactional
    public void deleteStartupById(Long id) {
        if (battleRepository.count() > 0) {
            throw new IllegalStateException("Cannot delete startups after the tournament has started.");
        }

        Startup startup = startupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Startup not found"));

        startupRepository.delete(startup);
    }

    public String generateHumanizedReport(Long id) {
        StartupHistoryDTO history = getStartupHistory(id);

        StringBuilder sb = new StringBuilder();
        sb.append("Startup Name: ").append(history.getStartup()).append(".\n\n");

        for (StartupBattleHistoryDTO battle : history.getBattles()) {
            sb.append("In round ").append(battle.getRound()).append(", ")
                    .append("they faced ").append(battle.getOpponent()).append(" and ")
                    .append("the result was ").append(battle.getResult()).append(". ");

            if (!battle.getEvents().isEmpty()) {
                sb.append("Events applied: ");
                for (StartupHistoryEventDTO event : battle.getEvents()) {
                    sb.append(event.getType()).append(" (").append(event.getPoints()).append(" pts), ");
                }
                sb.setLength(sb.length() - 2);
            } else {
                sb.append("No events were applied in this battle.");
            }
            sb.append("\n\n");
        }

        return sb.toString();
    }
}
