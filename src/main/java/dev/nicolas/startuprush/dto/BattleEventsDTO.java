package dev.nicolas.startuprush.dto;

import dev.nicolas.startuprush.model.EventType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BattleEventsDTO {
    private Long battleId;
    private List<EventType> eventsForStartupA;
    private List<EventType> getEventsForStartupB;
}
