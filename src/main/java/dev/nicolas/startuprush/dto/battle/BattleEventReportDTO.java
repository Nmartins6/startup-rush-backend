package dev.nicolas.startuprush.dto.battle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BattleEventReportDTO {
    private String type;
    private int points;
}
