package dev.nicolas.startuprush.controller;

import dev.nicolas.startuprush.dto.startup.*;
import dev.nicolas.startuprush.model.Startup;
import dev.nicolas.startuprush.service.StartupService;
import dev.nicolas.startuprush.service.StartupSummaryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/startups")
public class StartupController {

    private final StartupService startupService;
    private final StartupSummaryService startupSummaryService;

    public StartupController(StartupService startupService, StartupSummaryService startupSummaryService) {
        this.startupService = startupService;
        this.startupSummaryService = startupSummaryService;
    }

    @PostMapping
    public ResponseEntity<Startup> register(@RequestBody StartupDTO dto) {
        return ResponseEntity.ok(startupService.registerStartup(dto));
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<StartupHistoryDTO> getStartupHistory(@PathVariable Long id) {
        return ResponseEntity.ok(startupService.getStartupHistory(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Startup> updateStartup(@PathVariable Long id, @RequestBody @Valid UpdateStartupDTO dto) {
        return ResponseEntity.ok(startupService.updateStartup(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStartup(@PathVariable Long id) {
        startupService.deleteStartupById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<String> getStartupSummary(@PathVariable Long id) {
        String summary = startupService.generateHumanizedReport(id);
        return ResponseEntity.ok(summary);
    }
}
