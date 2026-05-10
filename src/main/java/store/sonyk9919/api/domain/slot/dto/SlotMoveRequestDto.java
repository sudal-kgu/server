package store.sonyk9919.api.domain.slot.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SlotMoveRequestDto {

    private Integer fromSlotNumber;
    private Integer toSlotNumber;

    public static SlotMoveRequestDto of(Integer fromSlotNumber, Integer toSlotNumber){
        return new SlotMoveRequestDto(fromSlotNumber, toSlotNumber);
    }

    @AssertTrue(message = "같은 슬롯간 이동은 불가능합니다.")
    public boolean isValidMove() {
        return !fromSlotNumber.equals(toSlotNumber);
    }
}
