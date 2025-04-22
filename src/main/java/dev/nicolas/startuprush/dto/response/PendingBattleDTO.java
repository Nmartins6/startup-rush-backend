package dev.nicolas.startuprush.dto.response;

public record PendingBattleDTO(
        Long battleId,
        String startupAName,
        String startupBName
) {}