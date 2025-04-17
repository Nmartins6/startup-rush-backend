package dev.nicolas.startuprush.controller;

import dev.nicolas.startuprush.dto.StartupReportDTO;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.model.StartupBattle;
import dev.nicolas.startuprush.service.StartupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/startups")
public class StartupController {

    private final StartupService startupService;

    public StartupController(StartupService startupService) {
        this.startupService = startupService;
    }

    @PostMapping
    public ResponseEntity<Startup> register(@RequestBody Startup startup) {
        return ResponseEntity.ok(startupService.registerStartup(startup));
    }

    @GetMapping
    public ResponseEntity<List<Startup>> getAll() {
        return ResponseEntity.ok(startupService.getAllStartups());
    }

    @DeleteMapping
    public ResponseEntity<Void> clearAll() {
        startupService.clearAllStartups();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<StartupReportDTO>> getRanking() {
        return ResponseEntity.ok(startupService.getRankingReport());
    }

}
