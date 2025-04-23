package dev.nicolas.startuprush.dto.battle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartupSummaryDTO {
    private Long id;
    private String name;
    private String slogan;
    private int score;
}
