package dev.nicolas.startuprush.controller.dev;

import dev.nicolas.startuprush.util.TournamentSeeder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/api/dev")
public class DevController {

    private final TournamentSeeder seeder;

    public DevController(TournamentSeeder seeder) {
        this.seeder = seeder;
    }

    @PostMapping("/seed")
    public ResponseEntity<String> runSeed(@RequestParam(defaultValue = "6") int quantity) {
        seeder.seedTournament(quantity);
        return ResponseEntity.ok("Seed executado com " + quantity + " startups.");
    }
}