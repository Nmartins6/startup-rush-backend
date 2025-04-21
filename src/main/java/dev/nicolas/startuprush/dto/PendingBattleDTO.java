package dev.nicolas.startuprush.dto;

public record PendingBattleDTO(
        Long battleId,
        String startupAName,
        String startupBName
) {}