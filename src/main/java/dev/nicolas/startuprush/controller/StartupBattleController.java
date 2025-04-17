package dev.nicolas.startuprush.controller;

import dev.nicolas.startuprush.dto.BattleEventsDTO;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.service.BattleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
