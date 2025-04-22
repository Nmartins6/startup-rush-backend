package dev.nicolas.startuprush.dto.startup;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StartupRankingDTO {
    private Long id;
    private String name;
    private int score;
}