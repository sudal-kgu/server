package store.sonyk9919.api.domain.slot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SlotActivateResponseDto {

    private Integer slotNumber;
    private long remainingShell;
}