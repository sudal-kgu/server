package store.sonyk9919.api.domain.building.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.island.dto.ResourceBalanceResponse;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildingResponseDto {

    private SlotResponseDto slot;
    private ResourceBalanceResponse resource;

    public static BuildingResponseDto of(SlotResponseDto slot, ResourceBalanceResponse resource) {
        return new BuildingResponseDto(slot, resource);
    }
}
