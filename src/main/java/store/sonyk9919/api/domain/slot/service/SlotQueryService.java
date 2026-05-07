package store.sonyk9919.api.domain.slot.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingDetailDto;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.building.dto.ProductionInfoDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.service.HarvestCalculator;
import store.sonyk9919.api.domain.building.service.IslandBoostCache;
import store.sonyk9919.api.domain.island.entity.LevelSpec;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.service.LevelSpecCache;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.slot.dto.SlotDetailResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotListResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotUnlockResource;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.entity.SlotUnlockPolicy;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlotQueryService {

    private final SlotRepository slotRepository;
    private final MemberIslandRegistryService memberIslandService;
    private final IslandBoostCache islandBoostCache;
    private final LevelSpecCache levelSpecCache;

    public SlotListResponseDto getAllSlot(Long memberId) {
        MemberIsland island = memberIslandService.getIsland(memberId);

        LevelSpec levelSpec = levelSpecCache.get(island.getLevel());
        int maxActivatableSlots = levelSpec.getMaxSlotCount();

        List<SlotResponseDto> slots = slotRepository.findAllByIsland(island)
                .stream()
                .map(slot -> SlotResponseDto.of(slot, mapToBuildingInfo(slot.getBuilding())))
                .collect(Collectors.toList());

        SlotUnlockResource resource = SlotUnlockResource.of(
                ResourceType.SHELL,
                SlotUnlockPolicy.costFor(slotRepository.countByIslandAndActivatedTrue(island))
        );

        return SlotListResponseDto.of(slots, maxActivatableSlots, resource);
    }

    private BuildingInfoDto mapToBuildingInfo(Building building){
        if (building == null) return null;

        return BuildingInfoDto.of(building, building.getBuildingMetadata());
    }

    public SlotDetailResponseDto getSlotDetail(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);

        Slot slot = slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));

        SlotUnlockResource slotUnlockResource = !slot.isActivated() ?
                calculateUnlockCost(island) : null;

        return SlotDetailResponseDto.of(
                slot,
                mapToBuildingDetail(island, slot.getBuilding()),
                slotUnlockResource
        );
    }

    private BuildingDetailDto mapToBuildingDetail(MemberIsland island, Building building) {
        if (building == null) return null;

        BuildingMetadata metadata = building.getBuildingMetadata();
        return BuildingDetailDto.of(
                building, metadata,
                createInfo(island, building, metadata));
    }

    private ProductionInfoDto createInfo(MemberIsland island, Building building, BuildingMetadata metadata) {
        if (!metadata.isProductionType()) return null;

        return ProductionInfoDto.of(
                building,
                metadata.getYieldForLevel(building.getCurrentLevel()),
                computeGems(island, building)
        );
    }

    private int computeGems(MemberIsland island, Building building) {
        double boostPercent = islandBoostCache.getTotalBoost(island);
        if (!building.canHarvest()) return 0;

        HarvestCalculator calculator = HarvestCalculator.of(building, LocalDateTime.now());
        return calculator.calculate(boostPercent);
    }

    private SlotUnlockResource calculateUnlockCost(MemberIsland island) {
        return SlotUnlockResource.of(
                ResourceType.SHELL,
                SlotUnlockPolicy.costFor(slotRepository.countByIslandAndActivatedTrue(island))
        );
    }
}