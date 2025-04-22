package dev.nicolas.startuprush.controller;

import dev.nicolas.startuprush.dto.request.BattleEventsRequestDTO;
import dev.nicolas.startuprush.dto.response.ChampionDTO;
import dev.nicolas.startuprush.dto.response.PendingBattleDTO;
import dev.nicolas.startuprush.dto.report.RoundReportDTO;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.service.BattleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/battles")
public class StartupBattleController {

    private final BattleService battleService;

    public StartupBattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @PostMapping("/events")
    public ResponseEntity<StartupBattle> applyBattleEvents(@RequestBody BattleEventsRequestDTO request) {
        return ResponseEntity.ok(battleService.applyBattleEvents(request));
    }

    @GetMapping("/draw")
    public ResponseEntity<StartupBattle> createRandomBattle() {
        return ResponseEntity.ok(battleService.createRandomBattle());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PendingBattleDTO>> getPendingBattles() {
        return ResponseEntity.ok(battleService.getCompactPendingBattles());
    }

    @PostMapping("/round/advance")
    public ResponseEntity<List<StartupBattle>> advanceRound() {
        return ResponseEntity.ok(battleService.startNextRound());
    }

    @GetMapping("/report")
    public ResponseEntity<List<RoundReportDTO>> getBattleReport() {
        return ResponseEntity.ok(battleService.generateRoundReport());
    }

    @GetMapping("/champion")
    public ResponseEntity<ChampionDTO> getChampion() {
        ChampionDTO champion = battleService.getChampion();
        return ResponseEntity.ok(champion);
    }

    @PostMapping("/start-tournament")
    public ResponseEntity<List<StartupBattle>> startTournament() {
        return ResponseEntity.ok(battleService.startTournament());
    }
}