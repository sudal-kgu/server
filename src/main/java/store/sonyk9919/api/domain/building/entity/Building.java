package store.sonyk9919.api.domain.building.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.global.common.exception.CustomException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_base_id", nullable = false)
    private BuildingBase buildingBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private MemberIsland island;

    @Column(nullable = false)
    private int currentLevel;

    private LocalDateTime lastCollectedAt;
    private LocalDateTime fuelExpiredAt;

    private Building(BuildingBase buildingBase) {
        this.buildingBase = buildingBase;
        this.currentLevel = 1;
    }

    public static Building of(BuildingBase buildingBase){
        return new Building(buildingBase);
    }

    public void operate(LocalDateTime now, int durationSecond) {
        if (!buildingBase.isProductionType()) {
            throw new CustomException(BuildingStatus.NOT_PRODUCTION_BUILDING);
        }

        lastCollectedAt = now;
        fuelExpiredAt = now.plusSeconds(durationSecond);
    }

    public boolean isOperating(LocalDateTime now) {
        if (fuelExpiredAt == null) return false;
        return now.isBefore(fuelExpiredAt);
    }

    public void updateCollectedTime(LocalDateTime now, LocalDateTime fuelExpiredAt) {
        if (fuelExpiredAt == null || lastCollectedAt == null) {
            throw new CustomException(BuildingStatus.NOT_OPERATING);
        }
        if (now.isBefore(lastCollectedAt)) {
            throw new CustomException(BuildingStatus.INVALID_COLLECT_TIME);
        }
        if (!lastCollectedAt.isBefore(fuelExpiredAt)) {
            throw new CustomException(BuildingStatus.NOT_OPERATING);
        }

        lastCollectedAt = now;
    }
}