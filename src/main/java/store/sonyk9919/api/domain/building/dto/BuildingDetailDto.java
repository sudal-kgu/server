package store.sonyk9919.api.domain.building.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildingDetailDto {

    private Long buildingId;
    private String name;
    private String category;
    private String model;
    private int currentLevel;

    private ProductionInfoDto productionInfo;

    public static BuildingDetailDto from(Building building) {
        if (building == null) return null;

        BuildingMetadata metadata = building.getBuildingMetadata();
        int level = building.getCurrentLevel();

        return new BuildingDetailDto(
                building.getId(),
                metadata.getName(),
                metadata.getCategory().name(),
                metadata.getModel(),
                level,
                ProductionInfoDto.from(building, metadata.getYieldForLevel(level))
        );
    }
}