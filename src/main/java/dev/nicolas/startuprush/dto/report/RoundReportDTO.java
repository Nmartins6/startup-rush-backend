package dev.nicolas.startuprush.dto.report;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoundReportDTO {
    private int round;
    private List<BattleReportDTO> battles;
}
