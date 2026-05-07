package store.sonyk9919.api.domain.slot.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SlotMoveDto {

    private final Integer fromSlotNumber;
    private final Integer toSlotNumber;

    public static SlotMoveDto of(Integer fromSlotNumber, Integer toSlotNumber){
        return new SlotMoveDto(fromSlotNumber, toSlotNumber);
    }
}
