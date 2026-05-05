package store.sonyk9919.api.domain.building.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class BuildingHarvestService {

    private final SlotRepository slotRepository;
    private final MemberIslandRegistryService memberIslandService;
    private final ResourceService resourceService;
    private final IslandBoostCache islandBoostCache;

    @DistributedLock(key = "'island:' + #memberId + ':harvest'")
    @Transactional
    public int harvest(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);
        Slot slot = slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));

        if(!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);
        Building building = slot.getBuilding();

        HarvestCalculator calculator = HarvestCalculator.of(building, LocalDateTime.now());
        int gems = computeGems(island, calculator);
        if (gems <= 0) throw new CustomException(BuildingStatus.NOTHING_TO_HARVEST);

        applyHarvest(building, island, gems, calculator);
        return gems;
    }

    private int computeGems(MemberIsland island, HarvestCalculator calculator) {
        double boostPercent = islandBoostCache.getTotalBoost(island);
        return calculator.calculate(boostPercent);
    }

    private void applyHarvest(Building building, MemberIsland island, int gems, HarvestCalculator calculator) {
        building.updateCollectedTime(calculator.getBaseTime());
        resourceService.add(island, ResourceType.GEM, gems);
    }

    @DistributedLock(key = "'island:' + #memberId + ':harvest'")
    @Transactional
    public int harvestAll(Long memberId) {
        MemberIsland island = memberIslandService.getIsland(memberId);
        double boostPercent = islandBoostCache.getTotalBoost(island);

        List<Slot> harvestableSlots = getHarvestableSlots(island);

        int totalGems = applyAndSumGems(harvestableSlots, boostPercent, LocalDateTime.now());
        if (totalGems <= 0) throw new CustomException(BuildingStatus.NOTHING_TO_HARVEST);

        resourceService.add(island, ResourceType.GEM, totalGems);
        return totalGems;
    }

    private List<Slot> getHarvestableSlots(MemberIsland island) {
        return slotRepository.findAllByIsland(island).stream()
                .filter(Slot::hasBuilding)
                .filter(slot -> slot.getBuilding().canHarvest())
                .collect(Collectors.toList());
    }

    private int applyAndSumGems(List<Slot> slots, double boostPercent, LocalDateTime now) {
        return slots.stream()
                .mapToInt(slot -> {
                    Building building = slot.getBuilding();
                    HarvestCalculator calculator = HarvestCalculator.of(building, now);
                    int gems = calculator.calculate(boostPercent);
                    if (gems > 0) building.updateCollectedTime(calculator.getBaseTime());
                    return gems;
                })
                .sum();
    }
}