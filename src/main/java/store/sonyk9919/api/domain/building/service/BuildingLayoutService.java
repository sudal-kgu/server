package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingLayoutDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.entity.BuildingYield;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.domain.building.repository.BuildingMetadataRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceBuildingService;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
public class BuildingLayoutService {

    private final SlotRepository slotRepository;
    private final BuildingMetadataRepository metadataRepository;
    private final MemberIslandRegistryService memberIslandService;
    private final ResourceBuildingService resourceService;
    private final IslandBoostCache islandBoostCache;

    @Transactional
    public void buildOf(Long memberId, Integer slotNumber, BuildingLayoutDto layoutDto) {
        Slot slot = getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        BuildingMetadata metadata = metadataRepository.findById(layoutDto.getBuildingMetadataId())
                .orElseThrow(() -> new CustomException(BuildingStatus.BUILDING_METADATA_NOT_FOUND));

        BuildingYield yield = metadata.getYieldForLevel(1);

        resourceService.subtractResource(
                island,
                yield.getCostShells(),
                yield.getCostGems()
        );

        slot.build(Building.of(island, metadata));
        islandBoostCache.evictBoostCache(island);
    }

    @Transactional
    public void demolishOf(Long memberId, Integer slotNumber) {
        Slot slot = getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        Building building = slot.getBuilding();
        if (building == null) throw new CustomException(SlotStatus.SLOT_EMPTY);

        BuildingYield yield = building.getCurrentYield();

        resourceService.addResource(
                island,
                yield.getRefundShell(),
                yield.getRefundGem()
        );

        slot.demolish();
        islandBoostCache.evictBoostCache(island);
    }

    private Slot getSlot(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);
        return slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));
    }
}