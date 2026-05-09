package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.slot.dto.SlotMoveDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.service.SlotQueryHelper;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class BuildingMoveService {

    private final SlotQueryHelper slotQueryHelper;

    @DistributedLock(keys = {
            "'slot:' + #memberId + ':' + #slotMoveDto.fromSlotNumber",
            "'slot:' + #memberId + ':' + #slotMoveDto.toSlotNumber"
    })
    @Transactional
    public void moveOf(Long memberId, SlotMoveDto slotMoveDto) {
        Integer fromSlotNumber = slotMoveDto.getFromSlotNumber();
        Integer toSlotNumber = slotMoveDto.getToSlotNumber();

        Slot fromSlot = slotQueryHelper.getSlot(memberId, fromSlotNumber);
        Slot toSlot = slotQueryHelper.getSlot(memberId, toSlotNumber);

        if (!fromSlot.hasBuilding()) throw new CustomException(SlotStatus.SLOT_EMPTY);

        fromSlot.swapBuildingWith(toSlot);
    }
}
