package dev.nicolas.startuprush.dto.battle;

import dev.nicolas.startuprush.dto.common.StartupSummaryDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BattleDetailsDTO {
    private Long battleId;
    private StartupSummaryDTO startupA;
    private StartupSummaryDTO startupB;
    private List<BattleEventReportDTO> eventsA;
    private List<BattleEventReportDTO> eventsB;
    private boolean completed;
    private Long winnerId;
}
