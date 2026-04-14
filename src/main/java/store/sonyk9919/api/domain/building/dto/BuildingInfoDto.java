package store.sonyk9919.api.domain.building.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.entity.Building;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildingInfoDto {

    private Long buildingId;
    private String buildingName;
    private String category;
    private String model;

    private int currentLevel;
    private boolean operating;

    public static BuildingInfoDto from(Building building) {
        if (building == null) return null;

        return new BuildingInfoDto(
                building.getId(),
                building.getBuildingMetadata().getName(),
                building.getBuildingMetadata().getCategory().name(),
                building.getBuildingMetadata().getModel(),
                building.getCurrentLevel(),
                building.isOperating(LocalDateTime.now())
        );
    }
}