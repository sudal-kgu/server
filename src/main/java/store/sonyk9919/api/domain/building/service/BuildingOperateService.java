package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingYield;
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
public class BuildingOperateService {

    private final SlotRepository slotRepository;
    private final MemberIslandRegistryService memberIslandService;
    private final ResourceService resourceService;

    @DistributedLock(key = "'slot:' + #memberId + ':' + #slotNumber")
    @Transactional
    public void operate(Long memberId, Integer slotNumber) {
        Slot slot = getSlot(memberId, slotNumber);

        if(!slot.hasBuilding()) throw new CustomException(BuildingStatus.BUILDING_NOT_FOUND);
        Building building = slot.getBuilding();
        BuildingYield yield = building.getCurrentYield();

        building.operate(yield.getDurationSecond());
        resourceService.subtract(slot.getIsland(), ResourceType.FUEL, yield.getRequiredFuel());
    }

    private Slot getSlot(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);
        return slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));
    }
}