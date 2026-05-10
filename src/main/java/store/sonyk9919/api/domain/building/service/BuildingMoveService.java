package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.slot.dto.SlotMoveRequestDto;
import store.sonyk9919.api.domain.slot.dto.SlotMoveResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
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
            "'slot:' + #memberId + ':' + #slotMoveRequestDto.fromSlotNumber",
            "'slot:' + #memberId + ':' + #slotMoveRequestDto.toSlotNumber"
    })
    @Transactional
    public SlotMoveResponseDto moveOf(Long memberId, SlotMoveRequestDto slotMoveRequestDto) {
        Integer fromSlotNumber = slotMoveRequestDto.getFromSlotNumber();
        Integer toSlotNumber = slotMoveRequestDto.getToSlotNumber();

        Slot fromSlot = slotQueryHelper.getSlot(memberId, fromSlotNumber);
        Slot toSlot = slotQueryHelper.getSlot(memberId, toSlotNumber);

        if (!fromSlot.hasBuilding()) throw new CustomException(SlotStatus.SLOT_EMPTY);

        fromSlot.swapBuildingWith(toSlot);

        return SlotMoveResponseDto.of(
                createSlotResponseDto(fromSlot),
                createSlotResponseDto(toSlot)
        );
    }

    private SlotResponseDto createSlotResponseDto(Slot slot) {
        if (!slot.hasBuilding()) return SlotResponseDto.from(slot);

        BuildingInfoDto buildingInfo = BuildingInfoDto.of(
                slot.getBuilding(),
                slot.getBuilding().getBuildingMetadata()
        );

        return SlotResponseDto.of(slot, buildingInfo);
    }
}
