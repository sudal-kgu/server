package store.sonyk9919.api.domain.island.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IslandLevelUpEvent {
    private final Long memberAccountId;
    private final Long islandId;
    private final int previousLevel;
    private final int newLevel;

    public static IslandLevelUpEvent of(Long memberAccountId, Long islandId, int previousLevel, int newLevel) {
        return new IslandLevelUpEvent(memberAccountId, islandId, previousLevel, newLevel);
    }
}
