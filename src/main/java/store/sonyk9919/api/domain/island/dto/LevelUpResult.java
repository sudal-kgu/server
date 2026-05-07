package store.sonyk9919.api.domain.island.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LevelUpResult {

    private final boolean levelUp;
    private final boolean reachedMaxLevel;
    private final int newLevel;
    private final List<String> unlockedItems;
    private final List<String> unlockedBuildings;

    public static LevelUpResult none() {
        return new LevelUpResult(false, false, 0, List.of(), List.of());
    }

    public static LevelUpResult of(int newLevel, boolean reachedMaxLevel, List<String> unlockedItems, List<String> unlockedBuildings) {
        return new LevelUpResult(true, reachedMaxLevel, newLevel, unlockedItems, unlockedBuildings);
    }
}
