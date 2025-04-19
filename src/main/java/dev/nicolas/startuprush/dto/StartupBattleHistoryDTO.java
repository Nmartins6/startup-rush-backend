package dev.nicolas.startuprush.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StartupBattleHistoryDTO {
    private int round;
    private String opponent;
    private String result;
    private List<StartupHistoryEventDTO> events;
}