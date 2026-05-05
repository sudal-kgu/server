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
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
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
    private final MemberIslandRegistryService memberIslandService;
    private final ResourceService resourceService;
    private final IslandBoostCache islandBoostCache;

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public void buildOf(Long memberId, Integer slotNumber, BuildingLayoutDto layoutDto) {
        Slot slot = getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        BuildingMetadata metadata = metadataRepository.findById(layoutDto.getBuildingMetadataId())
                .orElseThrow(() -> new CustomException(BuildingStatus.BUILDING_METADATA_NOT_FOUND));

        subtractResource(island, metadata.getYieldForLevel(1));
        slot.build(Building.of(island, metadata));
        islandBoostCache.evictBoostCache(island);
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

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public void demolishOf(Long memberId, Integer slotNumber) {
        Slot slot = getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        if(!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);
        Building building = slot.getBuilding();

        addResource(island, building.getCurrentYield());
        slot.demolish();
        islandBoostCache.evictBoostCache(island);
    }

    private void addResource(MemberIsland island, BuildingYield yield){
        if (yield.getRefundShell() > 0) {
            resourceService.add(island, ResourceType.SHELL, yield.getRefundShell());
        }
        if (yield.getRefundGem() > 0) {
            resourceService.add(island, ResourceType.GEM, yield.getRefundGem());
        }
    }

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public void levelUp(Long memberId, Integer slotNumber){
        Slot slot = getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        if(!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);
        Building building = slot.getBuilding();

        if (building.isOperating()) throw new CustomException(BuildingStatus.ALREADY_OPERATING);

        subtractResource(island, building.getNextYield());
        building.levelUp();
        islandBoostCache.evictBoostCache(island);
    }
}