package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingYield;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.service.SlotQueryHelper;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class BuildingUpgradeService {

    private final ResourceService resourceService;
    private final IslandBoostCache islandBoostCache;
    private final SlotQueryHelper slotQueryHelper;

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public void levelUp(Long memberId, Integer slotNumber){
        Slot slot = slotQueryHelper.getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        if (!slot.hasBuilding()) throw new CustomException(SlotStatus.SLOT_EMPTY);
        Building building = slot.getBuilding();

        if (building.isOperating()) throw new CustomException(BuildingStatus.ALREADY_OPERATING);

        subtractResource(island, building.getNextYield());
        building.levelUp();
        islandBoostCache.evictBoostCache(island);
    }

    private void subtractResource(MemberIsland island, BuildingYield yield){
        if (island.getLevel() < yield.getRequiredLevel()) {
            throw new CustomException(IslandStatus.LEVEL_TOO_LOW);
        }

        if (yield.getCostShells() > 0) {
            resourceService.subtract(island, ResourceType.SHELL, yield.getCostShells());
        }
        if (yield.getCostGems() > 0) {
            resourceService.subtract(island, ResourceType.GEM, yield.getCostGems());
        }
    }
}