package store.sonyk9919.api.domain.building.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.entity.Building;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildingInfoDto {

    private Long buildingId;
    private String name;
    private String category;
    private String modelUri;

    private int currentLevel;
    private LocalDateTime fuelExpiredAt;
    private LocalDateTime lastCollectedAt;

    private ProductionInfoDto productionInfo;

    public static BuildingInfoDto of(Building building, BuildingMetadata metadata) {
        return new BuildingInfoDto(
                building.getId(),
                metadata.getName(),
                metadata.getCategory().name(),
                metadata.getModel(),
                building.getCurrentLevel(),
                building.getFuelExpiredAt(),
                building.getLastCollectedAt(),
                null
        );
    }

    public static BuildingInfoDto of(Building building, BuildingMetadata metadata, ProductionInfoDto productionInfo) {
        return new BuildingInfoDto(
                building.getId(),
                metadata.getName(),
                metadata.getCategory().name(),
                metadata.getModel(),
                building.getCurrentLevel(),
                building.getFuelExpiredAt(),
                building.getLastCollectedAt(),
                productionInfo
        );
    }

    public static BuildingInfoDto of(Building building, BuildingMetadata metadata, String modelUri) {
        return new BuildingInfoDto(
                building.getId(),
                metadata.getName(),
                metadata.getCategory().name(),
                modelUri,
                building.getCurrentLevel(),
                building.getFuelExpiredAt(),
                building.getLastCollectedAt(),
                null
        );
    }

    public static BuildingInfoDto of(Building building, BuildingMetadata metadata, ProductionInfoDto productionInfo, String modelUri) {
        return new BuildingInfoDto(
                building.getId(),
                metadata.getName(),
                metadata.getCategory().name(),
                modelUri,
                building.getCurrentLevel(),
                building.getFuelExpiredAt(),
                building.getLastCollectedAt(),
                productionInfo
        );
    }
}