package store.sonyk9919.api.domain.building.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.sonyk9919.api.domain.building.exception.BuildingStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_metadata_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BuildingCategory category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String model;

    @OneToMany(mappedBy = "buildingMetadata")
    private List<BuildingYield> yields = new ArrayList<>();

    public boolean isProductionType() {
        return category == BuildingCategory.PRODUCTION;
    }

    public BuildingYield getYieldForLevel(int level) {
        return yields.stream()
                .filter(yield -> yield.getLevel() == level)
                .findFirst()
                .orElseThrow(() -> new CustomException(BuildingStatus.BUILDING_YIELD_NOT_FOUND));
    }
}