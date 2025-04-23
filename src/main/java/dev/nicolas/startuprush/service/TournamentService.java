package dev.nicolas.startuprush.service;

import dev.nicolas.startuprush.dto.tournament.TournamentStatusDTO;
import dev.nicolas.startuprush.repository.StartupBattleRepository;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {

    private final StartupBattleRepository battleRepository;

    public TournamentService(StartupBattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public TournamentStatusDTO getTournamentStatus() {
        var allBattles = battleRepository.findAll();

        boolean started = !allBattles.isEmpty();
        int currentRound = allBattles.stream()
                .map(b -> b.getRound())
                .max(Integer::compareTo)
                .orElse(0);
        int pendingBattles = (int) allBattles.stream()
                .filter(b -> !b.isCompleted())
                .count();
        boolean championDefined = allBattles.stream()
                .filter(b -> b.getWinner() != null)
                .count() == allBattles.size() && allBattles.size() > 0;

        return new TournamentStatusDTO(started, currentRound, pendingBattles, championDefined);
    }
}