package dev.nicolas.startuprush.util;

import dev.nicolas.startuprush.dto.BattleEventDTO;
import dev.nicolas.startuprush.dto.BattleEventsRequestDTO;
import dev.nicolas.startuprush.dto.ChampionDTO;
import dev.nicolas.startuprush.dto.StartupDTO;
import dev.nicolas.startuprush.model.BattleEvent;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.repository.BattleEventRepository;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import dev.nicolas.startuprush.repository.StartupRepository;
import dev.nicolas.startuprush.service.BattleService;
import dev.nicolas.startuprush.service.StartupService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TournamentSeeder {

    private final StartupService startupService;
    private final BattleService battleService;
    private final StartupRepository startupRepository;
    private final StartupBattleRepository battleRepository;
    private final BattleEventRepository battleEventRepository;

    public TournamentSeeder(StartupService startupService,
                            BattleService battleService,
                            StartupRepository startupRepository,
                            StartupBattleRepository battleRepository,
                            BattleEventRepository battleEventRepository) {
        this.startupService = startupService;
        this.battleService = battleService;
        this.startupRepository = startupRepository;
        this.battleRepository = battleRepository;
        this.battleEventRepository = battleEventRepository;
    }

    public void seedTournament(int quantity) {
        if (quantity < 4 || quantity > 8 || quantity % 2 != 0) {
            System.out.println("Erro: a quantidade de startups deve ser par, entre 4 e 8.");
            return;
        }

        System.out.println("== Seed de Torneio com " + quantity + " startups ==\n");

        startupRepository.deleteAll();
        battleRepository.deleteAll();
        battleEventRepository.deleteAll();

        for (int i = 1; i <= quantity; i++) {
            StartupDTO dto = new StartupDTO("Startup " + i, "Slogan " + i, 2025);
            startupService.registerStartup(dto);
            System.out.println("Criada: " + dto.getName());
        }

        battleService.startTournament();
        System.out.println("\n== Torneio iniciado ==\n");

        int round = 0;
        boolean hasNextRound = true;

        while (hasNextRound) {
            final int currentRound = round;

            List<StartupBattle> allBattlesThisRound = battleRepository.findAll()
                    .stream()
                    .filter(b -> b.getRound() == currentRound)
                    .toList();

            if (allBattlesThisRound.isEmpty()) {
                hasNextRound = false;
                break;
            }

            System.out.println("\nRodada " + currentRound + " - Verificando " + allBattlesThisRound.size() + " batalhas:");

            for (StartupBattle battle : allBattlesThisRound) {
                if (battle.getStartupB() == null) {
                    System.out.println("Startup " + battle.getStartupA().getName() + " avançou por BYE.");
                    continue;
                }

                if (battle.isCompleted()) {
                    continue;
                }

                List<BattleEventDTO> eventsA = Arrays.asList(
                        new BattleEventDTO("PITCH"),
                        new BattleEventDTO("USER_TRACTION")
                );
                List<BattleEventDTO> eventsB = Arrays.asList(
                        new BattleEventDTO("PITCH"),
                        new BattleEventDTO("USER_TRACTION")
                );

                Collections.shuffle(eventsA);
                Collections.shuffle(eventsB);

                battleService.applyBattleEvents(BattleEventsRequestDTO.builder()
                        .battleId(battle.getId())
                        .eventsForStartupA(eventsA)
                        .eventsForStartupB(eventsB)
                        .build());

                System.out.println("Batalha " + battle.getId() + ": " +
                        battle.getStartupA().getName() + " vs " + battle.getStartupB().getName());

                boolean sharkFightOccurred = battleEventRepository
                        .findByBattleId(battle.getId())
                        .stream()
                        .anyMatch(e -> e.getType().equals("SHARK_FIGHT"));

                if (sharkFightOccurred) {
                    System.out.println(" > SHARK_FIGHT ocorreu nesta batalha!");
                }
            }

            round++;
        }

        try {
            ChampionDTO champion = battleService.getChampion();
            System.out.println("\n== CAMPEÃ DO TORNEIO ==");
            System.out.println("Startup: " + champion.getName());
            System.out.println("Slogan: " + champion.getSlogan());
        } catch (Exception e) {
            System.out.println("\nErro ao buscar campeã: " + e.getMessage());
        }

        System.out.println("\nAcesse o relatório em:");
        System.out.println(" - /api/battles/report");
        System.out.println(" - /api/startups/{id}/history");
    }
}
