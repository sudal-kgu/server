package store.sonyk9919.api.domain.island.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LevelUpResult {

    private final MemberIslandDto island;
    private final UnlockNotice notice;

    public static LevelUpResult none() {
        return new LevelUpResult(null, null);
    }

    public static LevelUpResult of(MemberIslandDto island, UnlockNotice notice) {
        return new LevelUpResult(island, notice);
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UnlockNotice {
        private final boolean reachedMaxLevel;
        private final List<String> unlockedItems;
        private final List<String> unlockedBuildings;

        public static UnlockNotice of(boolean reachedMaxLevel, List<String> unlockedItems, List<String> unlockedBuildings) {
            return new UnlockNotice(reachedMaxLevel, unlockedItems, unlockedBuildings);
        }
    }
}
