package dev.nicolas.startuprush.controller.dev;

import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.service.BattleService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("dev")
@RestController
@RequestMapping("/api/dev/battles")
public class DevBattleController {

    private final BattleService battleService;

    public DevBattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping("/pending/full")
    public ResponseEntity<List<StartupBattle>> getPendingBattlesFull() {
        return ResponseEntity.ok(battleService.getPendingBattles());
    }
}
