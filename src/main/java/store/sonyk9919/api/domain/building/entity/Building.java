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

    @Column(nullable = false)
    private int currentLevel;

    private LocalDateTime lastFueledAt;
    private LocalDateTime lastCollectedAt;

    public Building(BuildingBase buildingBase) {
        this.buildingBase = buildingBase;
        this.currentLevel = 1;
    }

    public void operate(LocalDateTime now) {
        if (!buildingBase.isProductionType()) {
            throw new CustomException(BuildingStatus.NOT_PRODUCTION_BUILDING);
        }
        lastFueledAt = now;
        lastCollectedAt = now;
    }

    public boolean isOperating(LocalDateTime now) {
        if (lastFueledAt == null) return false;

        int duration = buildingBase.getSpecForLevel(currentLevel).getDurationSecond();
        LocalDateTime fuelExpiredAt = lastFueledAt.plusSeconds(duration);

        return now.isBefore(fuelExpiredAt);
    }

    public void updateCollectedTime(LocalDateTime now) {
        if (lastFueledAt == null || lastCollectedAt == null) {
            throw new CustomException(BuildingStatus.NOT_OPERATING);
        }
        if (now.isBefore(lastCollectedAt)) {
            throw new CustomException(BuildingStatus.INVALID_COLLECT_TIME);
        }

        lastCollectedAt = now;
    }
}