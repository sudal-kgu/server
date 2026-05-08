package store.sonyk9919.api.domain.building.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.entity.BuildingYield;

@Getter
@RequiredArgsConstructor
public class BuildingCatalogDto {

    private final Long buildingMetadataId;
    private final String name;
    private final String category;
    private final String model;

    private final int requiredLevel;
    private final int costShells;
    private final int costGems;
    private final int pph;

    public static BuildingCatalogDto of(BuildingMetadata metadata, BuildingYield yield) {
        return new BuildingCatalogDto(
                metadata.getId(),
                metadata.getName(),
                metadata.getCategory().name(),
                metadata.getModel(),
                yield.getRequiredLevel(),
                yield.getCostShells(),
                yield.getCostGems(),
                yield.getPph()
        );
    }
}