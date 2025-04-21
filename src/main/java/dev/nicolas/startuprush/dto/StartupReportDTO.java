package dev.nicolas.startuprush.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartupReportDTO {
    private String name;
    private String slogan;
    private int score;
    private int pitchCount;
    private int bugsCount;
    private int userTractionCount;
    private int investorAngerCount;
    private int fakeNewsCount;
    private int sharkFightCount;
    private boolean advanceByBye;
}
