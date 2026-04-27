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
    @JoinColumn(name = "building_metadata_id", nullable = false)
    private BuildingMetadata buildingMetadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "island_id", nullable = false)
    private MemberIsland island;

    @Column(nullable = false)
    private int currentLevel;

    private LocalDateTime lastCollectedAt;
    private LocalDateTime fuelExpiredAt;

    private Building(MemberIsland island, BuildingMetadata buildingMetadata) {
        this.buildingMetadata = buildingMetadata;
        this.island = island;
        this.currentLevel = 1;
    }

    public static Building of(MemberIsland island, BuildingMetadata buildingMetadata) {
        return new Building(island, buildingMetadata);
    }

    public BuildingYield getCurrentYield() {
        return buildingMetadata.getYieldForLevel(currentLevel);
    }

    public boolean isOperating() {
        LocalDateTime now = LocalDateTime.now();
        if (fuelExpiredAt == null) return false;
        return now.isBefore(fuelExpiredAt);
    }

    public boolean canHarvest() {
        if (fuelExpiredAt == null) return false;
        return buildingMetadata.isProductionType();
    }

    public void operate(int durationSecond) {
        if (!buildingMetadata.isProductionType()) {
            throw new CustomException(BuildingStatus.NOT_PRODUCTION_BUILDING);
        }
        LocalDateTime now = LocalDateTime.now();
        if (fuelExpiredAt != null && now.isBefore(fuelExpiredAt)) {
            throw new CustomException(BuildingStatus.ALREADY_OPERATING);
        }
        lastCollectedAt = now;
        fuelExpiredAt = now.plusSeconds(durationSecond);
    }

    public void updateCollectedTime(LocalDateTime baseTime) {
        if (fuelExpiredAt == null || lastCollectedAt == null) {
            throw new CustomException(BuildingStatus.NOT_OPERATING);
        }
        if (baseTime.isBefore(lastCollectedAt)) {
            throw new CustomException(BuildingStatus.INVALID_COLLECT_TIME);
        }
        lastCollectedAt = baseTime;
    }
}