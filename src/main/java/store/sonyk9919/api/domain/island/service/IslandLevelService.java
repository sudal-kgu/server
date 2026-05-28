package store.sonyk9919.api.domain.island.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;
import store.sonyk9919.api.domain.building.service.BuildingMetadataCache;
import store.sonyk9919.api.domain.island.dto.ItemCatalogDto;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;
import store.sonyk9919.api.domain.island.dto.NextLevelConditionDto;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IslandLevelService {

    private final LevelSpecCache levelSpecCache;
    private final ItemCache itemCache;
    private final BuildingMetadataCache buildingMetadataCache;
    private final MemberIslandRepository memberIslandRepository;
    private final IslandModelUriResolver islandModelUriResolver;

    @DistributedLock(key = "'island:' + #memberAccountId + ':exp'")
    @Transactional
    public LevelUpResult addRecyclingExp(Long memberAccountId) {
        MemberIsland island = memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
        LevelSpec currentSpec = getLevelSpec(island.getLevel());
        island.addRecyclingExp(currentSpec);
        return checkAndProcessLevelUp(island);
    }

    @DistributedLock(key = "'island:' + #memberAccountId + ':exp'")
    @Transactional
    public LevelUpResult addItemExp(Long memberAccountId, int expAmount) {
        MemberIsland island = memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
        island.addItemExp(expAmount);
        return checkAndProcessLevelUp(island);
    }

    private LevelUpResult checkAndProcessLevelUp(MemberIsland island) {
        if (island.isMaxLevel()) return LevelUpResult.noLevelUp(MemberIslandDto.from(island, islandModelUriResolver.resolve(island), null));

        LevelSpec currentSpec = getLevelSpec(island.getLevel());
        if (!island.canLevelUp(currentSpec)) return LevelUpResult.noLevelUp(MemberIslandDto.from(island, islandModelUriResolver.resolve(island), buildNextLevelCondition(island)));

        island.levelUp();
        return buildLevelUpResult(island);
    }

    private LevelUpResult buildLevelUpResult(MemberIsland island) {
        boolean reachedMaxLevel = island.isMaxLevel();
        int newLevel = island.getLevel();
        LevelSpec newLevelSpec = getLevelSpec(newLevel);
        if (reachedMaxLevel) {
            return LevelUpResult.of(
                    MemberIslandDto.from(island, islandModelUriResolver.resolve(island), null),
                    LevelUpResult.UnlockNotice.of(true, newLevelSpec.getMaxSlotCount(), List.of(), List.of())
            );
        }
        return LevelUpResult.of(
                MemberIslandDto.from(island, islandModelUriResolver.resolve(island), buildNextLevelCondition(island)),
                LevelUpResult.UnlockNotice.of(false, newLevelSpec.getMaxSlotCount(), getUnlockedItems(newLevel), getUnlockedBuildings(newLevel))
        );
    }

    private NextLevelConditionDto buildNextLevelCondition(MemberIsland island) {
        if (island.isMaxLevel()) return null;
        LevelSpec currentSpec = getLevelSpec(island.getLevel());
        return NextLevelConditionDto.from(currentSpec);
    }

    private List<ItemCatalogDto> getUnlockedItems(int newLevel) {
        return itemCache.getAll().stream()
                .filter(i -> i.getUnlockLevel() == newLevel)
                .map(i -> ItemCatalogDto.from(i, 0L, true))
                .toList();
    }

    private List<BuildingCatalogDto> getUnlockedBuildings(int newLevel) {
        return buildingMetadataCache.get().stream()
                .filter(b -> b.getRequiredLevel() == newLevel)
                .toList();
    }

    private LevelSpec getLevelSpec(int level) {
        return levelSpecCache.get(level);
    }
}
