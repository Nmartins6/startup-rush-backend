package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.startup.StartupBattleHistoryDTO;
import dev.nicolas.startuprush.dto.startup.StartupHistoryDTO;
import dev.nicolas.startuprush.dto.startup.StartupHistoryEventDTO;
import org.springframework.stereotype.Service;

@Service
public class StartupSummaryService {

    public String generateSummary(StartupHistoryDTO history) {
        StringBuilder summary = new StringBuilder();

        summary.append("Startup ").append(history.getStartup()).append(" participou do torneio Startup Rush.\n");

        for (StartupBattleHistoryDTO battle : history.getBattles()) {
            summary.append("- Na rodada ").append(battle.getRound())
                    .append(", enfrentou ").append(battle.getOpponent())
                    .append(" e ").append(
                            "WIN".equals(battle.getResult()) ? "venceu" : "foi derrotada"
                    ).append(".\n");

            for (StartupHistoryEventDTO event : battle.getEvents()) {
                summary.append("    • Evento: ").append(event.getType())
                        .append(" (+").append(event.getPoints()).append(" pontos)\n");
            }
        }

        summary.append("\nNo total, a startup acumulou uma trajetória marcada por ");
        summary.append(history.getBattles().size()).append(" batalhas, sendo ")
                .append(history.getBattles().stream().filter(b -> "WIN".equals(b.getResult())).count())
                .append(" vitórias.\n");

        return summary.toString();
    }
}
