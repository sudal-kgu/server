package store.sonyk9919.api.domain.building.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.entity.BuildingYield;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductionInfoDto {

    private int pph;
    private int expectedGem;

    public static ProductionInfoDto of(BuildingYield yield, int expectedGem) {
        return new ProductionInfoDto(
                yield.getPph(),
                expectedGem
        );
    }
}