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
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceBuildingService;
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
    private final ResourceBuildingService resourceService;
    private final IslandBoostCache islandBoostCache;

    @DistributedLock(key = "'island:' + #memberId + ':harvest'")
    @Transactional
    public int harvest(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);
        Slot slot = slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));

        Building building = slot.getBuilding();
        LocalDateTime now = LocalDateTime.now();

        int gems = computeGems(building, island, now);
        if (gems <= 0) throw new CustomException(BuildingStatus.NOTHING_TO_HARVEST);

        applyHarvest(building, island, gems, now);
        return gems;
    }

    @DistributedLock(key = "'island:' + #memberId + ':harvest'")
    @Transactional
    public int harvestAll(Long memberId) {
        MemberIsland island = memberIslandService.getIsland(memberId);
        LocalDateTime now = LocalDateTime.now();
        double boostPercent = islandBoostCache.getTotalBoost(island);

        List<Slot> harvestableSlots = getHarvestableSlots(island);
        int totalGems = sumGems(harvestableSlots, boostPercent, now);

        if (totalGems <= 0) throw new CustomException(BuildingStatus.NOTHING_TO_HARVEST);

        harvestableSlots.forEach(slot ->
                applyHarvestToBuilding(slot.getBuilding(), now, boostPercent)
        );
        resourceService.addGems(island, totalGems);

        return totalGems;
    }

    private int computeGems(Building building, MemberIsland island, LocalDateTime now) {
        double boostPercent = islandBoostCache.getTotalBoost(island);

        return HarvestCalculator.of(building, now)
                .calculate(boostPercent);
    }

    private int sumGems(List<Slot> slots, double boostPercent, LocalDateTime now) {
        return slots.stream()
                .mapToInt(slot ->
                        HarvestCalculator.of(slot.getBuilding(), now)
                                .calculate(boostPercent)
                )
                .sum();
    }

    private void applyHarvest(Building building, MemberIsland island, int gems, LocalDateTime now) {
        HarvestCalculator calc = HarvestCalculator.of(building, now);

        building.updateCollectedTime(calc.getBaseTime());
        resourceService.addGems(island, gems);
    }

    private void applyHarvestToBuilding(Building building, LocalDateTime now, double boostPercent) {
        HarvestCalculator calc = HarvestCalculator.of(building, now);

        if (calc.calculate(boostPercent) > 0) {
            building.updateCollectedTime(calc.getBaseTime());
        }
    }

    private List<Slot> getHarvestableSlots(MemberIsland island) {
        return slotRepository.findAllByIsland(island).stream()
                .filter(Slot::hasBuilding)
                .filter(slot -> slot.getBuilding().canHarvest())
                .collect(Collectors.toList());
    }
}