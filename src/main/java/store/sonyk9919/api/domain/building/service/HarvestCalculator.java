package store.sonyk9919.api.domain.building.service;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingYield;

@Getter
public class HarvestCalculator {

    private final Building building;
    private final LocalDateTime baseTime;

    private HarvestCalculator(Building building) {
        this.building = building;
        this.baseTime = resolveBaseTime(building, LocalDateTime.now());
    }

    private HarvestCalculator(Building building, LocalDateTime now) {
        this.building = building;
        this.baseTime = resolveBaseTime(building, now);
    }

    public static HarvestCalculator from(Building building) {
        return new HarvestCalculator(building);
    }

    public static HarvestCalculator of(Building building, LocalDateTime now) {
        return new HarvestCalculator(building, now);
    }

    public int calculate(double boostPercent) {
        double baseProduction = computeBaseProduction();
        if (baseProduction <= 0) return 0;

        double multiplier = 1.0 + (boostPercent / 100.0);
        return (int) (baseProduction * multiplier);
    }

    private static LocalDateTime resolveBaseTime(Building building, LocalDateTime now) {
        LocalDateTime expiredAt = building.getFuelExpiredAt();
        if (expiredAt == null) return now;
        return now.isBefore(expiredAt) ? now : expiredAt;
    }

    private double computeBaseProduction() {
        LocalDateTime lastCollected = building.getLastCollectedAt();
        if (lastCollected == null || building.getFuelExpiredAt() == null) return 0;

        long activeSeconds = Duration.between(lastCollected, baseTime).getSeconds();
        if (activeSeconds <= 0) return 0;

        BuildingYield yield = building.getCurrentYield();
        return (activeSeconds / 3600.0) * yield.getPph();
    }
}