package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.building.dto.BuildingLayoutDto;
import store.sonyk9919.api.domain.building.dto.BuildingResponseDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.entity.BuildingYield;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.domain.building.repository.BuildingMetadataRepository;
import store.sonyk9919.api.domain.building.repository.BuildingRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class BuildingLayoutService {

    private final SlotRepository slotRepository;
    private final BuildingMetadataRepository metadataRepository;
    private final BuildingRepository buildingRepository;
    private final MemberIslandRegistryService memberIslandService;
    private final ResourceService resourceService;
    private final IslandBoostCache islandBoostCache;

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public BuildingResponseDto buildOf(Long memberId, Integer slotNumber, BuildingLayoutDto layoutDto) {
        Slot slot = getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        BuildingMetadata metadata = metadataRepository.findById(layoutDto.getBuildingMetadataId())
                .orElseThrow(() -> new CustomException(BuildingStatus.BUILDING_METADATA_NOT_FOUND));

        subtractResource(island, metadata.getYieldForLevel(1));

        Building building = Building.of(island, metadata);
        slot.build(building);
        buildingRepository.save(building);

        islandBoostCache.evictBoostCache(island);
        return createResponse(memberId, slot, building);
    }

    private Slot getSlot(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);
        return slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));
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

    private BuildingResponseDto createResponse(Long memberId, Slot slot, Building building) {
        BuildingInfoDto buildingInfo = BuildingInfoDto.of(building, building.getBuildingMetadata());

        return BuildingResponseDto.of(
                SlotResponseDto.of(slot, buildingInfo),
                resourceService.getBalance(memberId)
        );
    }

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public BuildingResponseDto demolishOf(Long memberId, Integer slotNumber) {
        Slot slot = getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        if(!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);
        Building building = slot.getBuilding();

        addResource(island, building.getCurrentYield());
        slot.demolish();
        islandBoostCache.evictBoostCache(island);

        return BuildingResponseDto.of(
                SlotResponseDto.of(slot, null),
                resourceService.getBalance(memberId)
        );
    }

    private void addResource(MemberIsland island, BuildingYield yield){
        if (yield.getRefundShell() > 0) {
            resourceService.add(island, ResourceType.SHELL, yield.getRefundShell());
        }
        if (yield.getRefundGem() > 0) {
            resourceService.add(island, ResourceType.GEM, yield.getRefundGem());
        }
    }
}