package store.sonyk9919.api.domain.slot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlotMoveResponseDto {

    private SlotResponseDto fromSlot;
    private SlotResponseDto toSlot;

    public static SlotMoveResponseDto of(SlotResponseDto fromSlot, SlotResponseDto toSlot) {
        return new SlotMoveResponseDto(fromSlot, toSlot);
    }
}
