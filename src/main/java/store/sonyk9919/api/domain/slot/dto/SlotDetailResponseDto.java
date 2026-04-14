package store.sonyk9919.api.domain.slot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.dto.BuildingDetailDto;
import store.sonyk9919.api.domain.slot.entity.Slot;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlotDetailResponseDto {

    private Integer slotNumber;
    private boolean activated;
    private BuildingDetailDto building;

    public static SlotDetailResponseDto from(Slot slot) {
        return new SlotDetailResponseDto(
                slot.getSlotNumber(),
                slot.isActivated(),
                BuildingDetailDto.from(slot.getBuilding())
        );
    }
}