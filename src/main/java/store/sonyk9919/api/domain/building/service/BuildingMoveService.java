package store.sonyk9919.api.domain.building.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.slot.dto.SlotMoveRequestDto;
import store.sonyk9919.api.domain.slot.dto.SlotMoveResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.domain.slot.service.SlotQueryHelper;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.common.lock.DistributedLock;

@Service
@RequiredArgsConstructor
public class BuildingMoveService {

    private final SlotQueryHelper slotQueryHelper;
    private final SlotRepository slotRepository;
    private final BuildingModelUriResolver buildingModelUriResolver;

    @DistributedLock(keys = {
            "'slot:' + #memberId + ':' + #slotMoveRequestDto.fromSlotNumber",
            "'slot:' + #memberId + ':' + #slotMoveRequestDto.toSlotNumber"
    })
    @Transactional
    public SlotMoveResponseDto moveOf(Long memberId, SlotMoveRequestDto slotMoveRequestDto) {
        Slot fromSlot = slotQueryHelper.getSlot(memberId, slotMoveRequestDto.getFromSlotNumber());
        Slot toSlot = slotQueryHelper.getSlot(memberId, slotMoveRequestDto.getToSlotNumber());

        if (!fromSlot.hasBuilding()) throw new CustomException(SlotStatus.SLOT_EMPTY);

        Building fromBuilding = fromSlot.getBuilding();
        Building toBuilding = toSlot.getBuilding();
        fromSlot.clearBuilding();
        toSlot.clearBuilding();

        slotRepository.flush();
        fromSlot.assignBuilding(toBuilding);
        toSlot.assignBuilding(fromBuilding);

        return SlotMoveResponseDto.of(
                createSlotResponseDto(fromSlot),
                createSlotResponseDto(toSlot)
        );
    }

    private SlotResponseDto createSlotResponseDto(Slot slot) {
        if (!slot.hasBuilding()) return SlotResponseDto.from(slot);

        BuildingInfoDto buildingInfo = BuildingInfoDto.of(
                slot.getBuilding(),
                slot.getBuilding().getBuildingMetadata(),
                buildingModelUriResolver.resolve(slot.getBuilding().getBuildingMetadata())
        );
        return SlotResponseDto.of(slot, buildingInfo);
    }
}
