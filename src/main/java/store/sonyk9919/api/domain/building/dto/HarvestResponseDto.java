package store.sonyk9919.api.domain.building.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HarvestResponseDto {

    private int gems;

    public static HarvestResponseDto from(int expectedGems){
        return new HarvestResponseDto(expectedGems);
    }
}
