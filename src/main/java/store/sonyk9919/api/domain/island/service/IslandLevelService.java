package store.sonyk9919.api.domain.island.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;
import store.sonyk9919.api.domain.building.service.BuildingMetadataCache;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.entity.Item;
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
        if (island.isMaxLevel()) return LevelUpResult.none();

        LevelSpec nextSpec = getLevelSpec(island.getLevel() + 1);
        if (!island.canLevelUp(nextSpec)) return LevelUpResult.none();

        island.levelUp();
        return buildLevelUpResult(island.getLevel(), island.isMaxLevel());
    }

    private LevelUpResult buildLevelUpResult(int newLevel, boolean reachedMaxLevel) {
        List<String> unlockedItems = itemCache.getAll().stream()
                .filter(i -> i.getUnlockLevel() == newLevel)
                .map(Item::getName)
                .toList();
        List<String> unlockedBuildings = buildingMetadataCache.get().stream()
                .filter(b -> b.getRequiredLevel() == newLevel)
                .map(BuildingCatalogDto::getName)
                .toList();
        return LevelUpResult.of(newLevel, reachedMaxLevel, unlockedItems, unlockedBuildings);
    }

    private LevelSpec getLevelSpec(int level) {
        return levelSpecCache.get(level);
    }
}
