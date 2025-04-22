package dev.nicolas.startuprush.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartupHistoryEventDTO {
    private String type;
    private int points;
}
