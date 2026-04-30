package store.sonyk9919.api.domain.building.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingYield;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductionInfoDto {

    private int pph;
    private boolean operating;
    private LocalDateTime lastCollectedAt;
    private LocalDateTime fuelExpiredAt;
    private int expectedGem;

    public static ProductionInfoDto of(Building building, BuildingYield yield, int expectedGem) {
        return new ProductionInfoDto(
                yield.getPph(),
                building.isOperating(),
                building.getLastCollectedAt(),
                building.getFuelExpiredAt(),
                expectedGem
        );
    }
}