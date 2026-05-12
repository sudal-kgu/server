package store.sonyk9919.api.domain.slot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SlotActivateResponseDto {

    private SlotResponseDto slot;
    private ResourceBalanceResponse resource;
    private SlotUnlockResource nextCost;

    public static SlotActivateResponseDto of(SlotResponseDto slot, ResourceBalanceResponse resource, SlotUnlockResource nextCost){
        return new SlotActivateResponseDto(
                slot,
                resource,
                nextCost
        );
    }
}