package store.sonyk9919.api.domain.building.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.entity.BuildingYield;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BuildingCatalogDto {

    private Long buildingMetadataId;
    private String name;
    private String category;
    private String model;

    private int requiredLevel;
    private int costShells;
    private int costGems;
    private int pph;

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