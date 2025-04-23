package dev.nicolas.startuprush.controller;

import dev.nicolas.startuprush.dto.tournament.TournamentStatusDTO;
import dev.nicolas.startuprush.service.TournamentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/status")
    public TournamentStatusDTO getStatus() {
        return tournamentService.getTournamentStatus();
    }
}
