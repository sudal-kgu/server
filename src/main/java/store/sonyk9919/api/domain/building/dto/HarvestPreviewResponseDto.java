package store.sonyk9919.api.domain.building.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HarvestPreviewResponseDto {

    private int gem;

    public static HarvestPreviewResponseDto from(int expectedGem) {
        return new HarvestPreviewResponseDto(expectedGem);
    }
}
