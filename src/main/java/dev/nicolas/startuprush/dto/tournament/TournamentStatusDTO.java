package dev.nicolas.startuprush.dto.tournament;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentStatusDTO {
    private boolean started;
    private int currentRound;
    private int pendingBattles;
    private boolean championDefined;
}
