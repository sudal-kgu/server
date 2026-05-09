package store.sonyk9919.api.domain.slot.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlotListResponseDto {
    private List<SlotResponseDto> slots;
    private int maxActivatableSlots;

    public static SlotListResponseDto of(List<SlotResponseDto> slots, int maxActivatableSlots){
        return new SlotListResponseDto(slots, maxActivatableSlots);
    }
}
