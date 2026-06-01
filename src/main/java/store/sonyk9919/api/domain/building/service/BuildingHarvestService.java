package store.sonyk9919.api.domain.building.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.building.dto.HarvestPreviewResponseDto;
import store.sonyk9919.api.domain.building.dto.HarvestResponseDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.domain.slot.service.SlotQueryHelper;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class BuildingHarvestService {

    private final SlotRepository slotRepository;
    private final SlotQueryHelper slotQueryHelper;
    private final MemberIslandRegistryService memberIslandService;
    private final ResourceService resourceService;
    private final IslandBoostCache islandBoostCache;
    private final BuildingModelUriResolver buildingModelUriResolver;

    @DistributedLock(key = "'island:' + #memberId + ':harvest'")
    @Transactional
    public HarvestResponseDto harvest(Long memberId, Integer slotNumber) {
        Slot slot = slotQueryHelper.getSlot(memberId, slotNumber);
        MemberIsland island = slot.getIsland();

        if(!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);
        Building building = slot.getBuilding();

        double boostPercent = islandBoostCache.getTotalBoost(island);
        HarvestCalculator calculator = HarvestCalculator.from(building);
        int gems = calculator.calculate(boostPercent);
        if (gems <= 0) throw new CustomException(BuildingStatus.NOTHING_TO_HARVEST);

        MemberResource islandGem = applyHarvest(building, island, gems, calculator);
        int remainingGem = HarvestCalculator.from(building).calculate(boostPercent);
        SlotResponseDto slotDto = SlotResponseDto.of(slot, BuildingInfoDto.of(building, building.getBuildingMetadata(), buildingModelUriResolver.resolve(building.getBuildingMetadata())));
        return HarvestResponseDto.from(remainingGem, islandGem, slotDto);
    }

    private MemberResource applyHarvest(Building building, MemberIsland island, int gems, HarvestCalculator calculator) {
        building.updateCollectedTime(calculator.getBaseTime());
        return resourceService.add(island, ResourceType.GEM, gems);
    }

    @DistributedLock(key = "'island:' + #memberId + ':harvest'")
    @Transactional
    public HarvestResponseDto harvestAll(Long memberId) {
        MemberIsland island = memberIslandService.getIsland(memberId);
        double boostPercent = islandBoostCache.getTotalBoost(island);

        List<Slot> harvestableSlots = getHarvestableSlots(island);

        int totalGems = applyAndSumGems(harvestableSlots, boostPercent);
        if (totalGems <= 0) throw new CustomException(BuildingStatus.NOTHING_TO_HARVEST);

        MemberResource gem = resourceService.add(island, ResourceType.GEM, totalGems);
        List<SlotResponseDto> slotDtos = harvestableSlots.stream()
                .map(slot -> SlotResponseDto.of(slot, BuildingInfoDto.of(slot.getBuilding(), slot.getBuilding().getBuildingMetadata(), buildingModelUriResolver.resolve(slot.getBuilding().getBuildingMetadata()))))
                .collect(Collectors.toList());
        return HarvestResponseDto.from(gem, slotDtos);
    }

    private List<Slot> getHarvestableSlots(MemberIsland island) {
        return slotRepository.findAllByIsland(island).stream()
                .filter(Slot::hasBuilding)
                .filter(slot -> slot.getBuilding().canHarvest())
                .collect(Collectors.toList());
    }

    private int applyAndSumGems(List<Slot> slots, double boostPercent) {
        return slots.stream()
                .mapToInt(slot -> {
                    Building building = slot.getBuilding();
                    HarvestCalculator calculator = HarvestCalculator.from(building);
                    int gems = calculator.calculate(boostPercent);
                    if (gems > 0) building.updateCollectedTime(calculator.getBaseTime());
                    return gems;
                })
                .sum();
    }

    @Transactional(readOnly = true)
    public HarvestPreviewResponseDto preview(Long memberId, Integer slotNumber) {
        Slot slot = slotQueryHelper.getSlot(memberId, slotNumber);

        if (!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);

        Building building = slot.getBuilding();
        double totalBoost = islandBoostCache.getTotalBoost(slot.getIsland());
        int expectedGems = HarvestCalculator.from(building)
                .calculate(totalBoost);

        return HarvestPreviewResponseDto.from(expectedGems);
    }
}