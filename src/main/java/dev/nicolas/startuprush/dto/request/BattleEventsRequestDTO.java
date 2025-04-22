package dev.nicolas.startuprush.dto.request;

import dev.nicolas.startuprush.dto.common.BattleEventDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class BattleEventsRequestDTO {
    private Long battleId;
    private List<BattleEventDTO> eventsForStartupA;
    private List<BattleEventDTO> eventsForStartupB;
}
