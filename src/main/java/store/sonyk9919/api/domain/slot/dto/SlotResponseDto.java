package store.sonyk9919.api.domain.slot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.dto.BuildingInfoDto;
import store.sonyk9919.api.domain.slot.entity.Slot;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlotResponseDto {

    private Integer slotNumber;
    private boolean activated;
    private BuildingInfoDto building;

    public static SlotResponseDto from(Slot slot) {
        return new SlotResponseDto(
                slot.getSlotNumber(),
                slot.isActivated(),
                null
        );
    }

    public static SlotResponseDto of(Slot slot, BuildingInfoDto buildingInfo) {
        return new SlotResponseDto(
                slot.getSlotNumber(),
                slot.isActivated(),
                buildingInfo
        );
    }
}