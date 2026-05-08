package store.sonyk9919.api.domain.slot.dto;

import jakarta.validation.constraints.AssertTrue;
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

    @AssertTrue(message = "같은 슬롯간 이동은 불가능합니다.")
    public boolean isValidMove() {
        return !fromSlotNumber.equals(toSlotNumber);
    }
}
