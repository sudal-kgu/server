package store.sonyk9919.api.domain.slot.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SlotMoveDto {

    private Integer fromSlotNumber;
    private Integer toSlotNumber;
}
