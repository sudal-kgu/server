package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.building.dto.BuildingResponseDto;
import store.sonyk9919.api.domain.building.dto.OperationPreviewDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.entity.BuildingYield;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.service.SlotQueryHelper;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class BuildingOperateService {

    private final ResourceService resourceService;
    private final SlotQueryHelper slotQueryHelper;
    private final BuildingModelUriResolver buildingModelUriResolver;

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public BuildingResponseDto operate(Long memberId, Integer slotNumber) {
        Slot slot = slotQueryHelper.getSlot(memberId, slotNumber);

        if(!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);
        Building building = slot.getBuilding();
        BuildingYield yield = building.getCurrentYield();
        BuildingMetadata buildingMetadata = building.getBuildingMetadata();

        building.operate(yield.getDurationSecond());
        resourceService.subtract(slot.getIsland(), ResourceType.FUEL, yield.getRequiredFuel());
        return BuildingResponseDto.of(
                SlotResponseDto.of(slot, BuildingInfoDto.of(building, buildingMetadata, buildingModelUriResolver.resolve(buildingMetadata))),
                resourceService.getBalance(memberId)
        );
    }

    @Transactional(readOnly = true)
    public OperationPreviewDto preview(Long memberId, Integer slotNumber) {
        Slot slot = slotQueryHelper.getSlot(memberId, slotNumber);
        if (!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);

        Building building = slot.getBuilding();
        if (!building.getBuildingMetadata().isProductionType()) {
            throw new CustomException(BuildingStatus.NOT_PRODUCTION_BUILDING);
        }

        return OperationPreviewDto.from(
                building.getCurrentYield()
                        .getRequiredFuel()
        );
    }
}