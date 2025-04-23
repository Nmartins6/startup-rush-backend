package dev.nicolas.startuprush.dto.report;

import dev.nicolas.startuprush.dto.battle.BattleEventReportDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BattleReportDTO {
    private Long battleId;
    private String startupA;
    private String startupB;
    private String winner;
    private List<BattleEventReportDTO> eventsA;
    private List<BattleEventReportDTO> eventsB;
    private boolean advanceByBye;
}