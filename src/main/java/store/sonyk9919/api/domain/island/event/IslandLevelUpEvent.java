package store.sonyk9919.api.domain.island.event;

public record IslandLevelUpEvent(
        Long memberAccountId,
        Long islandId,
        int previousLevel,
        int newLevel
) {}
