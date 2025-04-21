package dev.nicolas.startuprush.controller.dev;

import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.service.BattleService;
import dev.nicolas.startuprush.util.TournamentSeeder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("dev")
@RestController
@RequestMapping("/api/dev")
public class DevController {

    private final TournamentSeeder seeder;
    private final BattleService battleService;

    public DevController(TournamentSeeder seeder) {
        this.seeder = seeder;
        this.battleService = battleService;
    }

    @PostMapping("/seed")
    public ResponseEntity<String> runSeed(@RequestParam(defaultValue = "6") int quantity) {
        seeder.seedTournament(quantity);
        return ResponseEntity.ok("Seed executado com " + quantity + " startups.");
    }

    @GetMapping("/pending/full")
    public ResponseEntity<List<StartupBattle>> getPendingBattlesFull() {
        return ResponseEntity.ok(battleService.getPendingBattles());
    }
}