package dev.nicolas.startuprush.dto.battle;

public record PendingBattleDTO(
        Long battleId,
        String startupAName,
        String startupBName
) {}