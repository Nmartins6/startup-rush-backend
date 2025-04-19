package dev.nicolas.startuprush.controller;

import dev.nicolas.startuprush.dto.BattleEventsRequestDTO;
import dev.nicolas.startuprush.dto.RoundReportDTO;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.service.BattleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<StartupBattle>> getPendingBattles() {
        return ResponseEntity.ok(battleService.getPendingBattles());
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
    public ResponseEntity<Map<String, String>> getChampion() {
        Startup champion = battleService.getChampion();
        Map<String, String> response = new HashMap<>();
        response.put("name", champion.getName());
        response.put("slogan", champion.getSlogan());
        return ResponseEntity.ok(response);
    }
}