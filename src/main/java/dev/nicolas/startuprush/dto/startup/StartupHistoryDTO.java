package dev.nicolas.startuprush.dto.startup;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StartupHistoryDTO {
    private String startup;
    private List<StartupBattleHistoryDTO> battles;
}

