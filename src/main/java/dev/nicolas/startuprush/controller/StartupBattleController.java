package dev.nicolas.startuprush.controller;

import dev.nicolas.startuprush.dto.BattleEventsDTO;
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
    public ResponseEntity<StartupBattle> applyBattleEvents(@RequestBody BattleEventsDTO dto) {
        StartupBattle result = battleService.applyBattleEvents(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/draw")
    public ResponseEntity<StartupBattle> createRandomBattle() {
        return ResponseEntity.ok(battleService.createRandomBattle());
    }


    @GetMapping("/pending")
    public ResponseEntity<List<StartupBattle>> getPendingBattles() {
        return ResponseEntity.ok(battleService.getPendingBattles());
    }

}
