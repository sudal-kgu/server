package store.sonyk9919.api.domain.building.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildingInfoDto {

    private Long buildingId;
    private String buildingName;
    private String category;
    private String model;

    private int currentLevel;
    private boolean operating;

    public static BuildingInfoDto of(Building building, BuildingMetadata metadata) {
        return new BuildingInfoDto(
                building.getId(),
                metadata.getName(),
                metadata.getCategory().name(),
                metadata.getModel(),
                building.getCurrentLevel(),
                building.isOperating()
        );
    }
}