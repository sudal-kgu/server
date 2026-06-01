package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.building.dto.BuildingResponseDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingYield;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.service.SlotQueryHelper;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class BuildingUpgradeService {

    private final ResourceService resourceService;
    private final SlotQueryHelper slotQueryHelper;
    private final ApplicationEventPublisher eventPublisher;
    private final BuildingModelUriResolver buildingModelUriResolver;

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public BuildingResponseDto levelUp(Long memberId, Integer slotNumber){
        Slot slot = slotQueryHelper.getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        if (!slot.hasBuilding()) throw new CustomException(SlotStatus.SLOT_EMPTY);
        Building building = slot.getBuilding();

        if (building.isOperating()) throw new CustomException(BuildingStatus.ALREADY_OPERATING);

        subtractResource(island, building.getNextYield());
        building.levelUp();
        eventPublisher.publishEvent(island);

        return BuildingResponseDto.of(
                SlotResponseDto.of(slot, mapToBuildingInfo(building)),
                resourceService.getBalance(memberId)
        );
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

    private BuildingInfoDto mapToBuildingInfo(Building building){
        if (building == null) return null;

        return BuildingInfoDto.of(building, building.getBuildingMetadata(), buildingModelUriResolver.resolve(building.getBuildingMetadata()));
    }
}