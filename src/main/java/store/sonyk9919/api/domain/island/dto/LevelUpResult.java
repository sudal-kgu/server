package store.sonyk9919.api.domain.island.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LevelUpResult {

    private final MemberIslandDto island;
    private final UnlockNotice notice;

    public static LevelUpResult noLevelUp(MemberIslandDto island) {
        return new LevelUpResult(island, null);
    }

    public static LevelUpResult of(MemberIslandDto island, UnlockNotice notice) {
        return new LevelUpResult(island, notice);
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UnlockNotice {
        private final boolean reachedMaxLevel;
        private final int maxSlotCount;
        private final List<ItemCatalogDto> unlockedItems;
        private final List<BuildingCatalogDto> unlockedBuildings;

        public static UnlockNotice of(boolean reachedMaxLevel, int maxSlotCount, List<ItemCatalogDto> unlockedItems, List<BuildingCatalogDto> unlockedBuildings) {
            return new UnlockNotice(reachedMaxLevel, maxSlotCount, unlockedItems, unlockedBuildings);
        }
    }
}
