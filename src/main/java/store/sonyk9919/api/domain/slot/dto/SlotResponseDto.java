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
    private SlotUnlockResource resource;

    public static SlotResponseDto from(Slot slot) {
        return new SlotResponseDto(
                slot.getSlotNumber(),
                slot.isActivated(),
                null, null
        );
    }

    public static SlotResponseDto of(Slot slot, BuildingInfoDto buildingDto, SlotUnlockResource resource) {
        return new SlotResponseDto(
                slot.getSlotNumber(),
                slot.isActivated(),
                buildingDto,
                resource
        );
    }

    public static SlotResponseDto of(Slot slot, BuildingInfoDto buildingDto) {
        return new SlotResponseDto(
                slot.getSlotNumber(),
                slot.isActivated(),
                buildingDto,
                null
        );
    }
}