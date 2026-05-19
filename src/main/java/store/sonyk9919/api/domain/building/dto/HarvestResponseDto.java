package store.sonyk9919.api.domain.building.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;

@Getter
public class HarvestResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL) private BuildingState building;
    private IslandResource resource;
    private List<SlotResponseDto> slots;

    private HarvestResponseDto(BuildingState building, IslandResource resource, List<SlotResponseDto> slots) {
        this.building = building;
        this.resource = resource;
        this.slots = slots;
    }

    public static HarvestResponseDto from(int remainingGem, MemberResource islandGem, SlotResponseDto slot) {
        return new HarvestResponseDto(BuildingState.of(remainingGem), IslandResource.of(islandGem), List.of(slot));
    }

    public static HarvestResponseDto from(MemberResource islandGem, List<SlotResponseDto> slots) {
        return new HarvestResponseDto(null, IslandResource.of(islandGem), slots);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class BuildingState {
        private final long gem;

        public static BuildingState of(long remainingGems) {
            return new BuildingState(remainingGems);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class IslandResource {
        private final long gem;

        public static IslandResource of(MemberResource gem) {
            return new IslandResource(gem.getAmount());
        }
    }
}
