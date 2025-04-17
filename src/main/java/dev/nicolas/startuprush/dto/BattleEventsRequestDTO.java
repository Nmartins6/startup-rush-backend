package dev.nicolas.startuprush.dto;

import lombok.Data;

import java.util.List;

@Data
public class BattleEventsRequestDTO {
    private Long battleId;
    private List<BattleEventDTO> eventsForStartupA;
    private List<BattleEventDTO> eventsForStartupB;
}
