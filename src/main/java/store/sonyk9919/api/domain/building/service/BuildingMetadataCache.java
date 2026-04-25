package store.sonyk9919.api.domain.building.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.repository.BuildingMetadataRepository;

@Component
@RequiredArgsConstructor
public class BuildingMetadataCache {
    private final BuildingMetadataRepository metadataRepository;

    @Cacheable(cacheNames = "buildingCatalog")
    public List<BuildingCatalogDto> get() {
        return metadataRepository.findAllWithLevelOneYield().stream()
                .map(this::mapToCatalog)
                .collect(Collectors.toList());
    }

    private BuildingCatalogDto mapToCatalog(BuildingMetadata metadata) {
        return BuildingCatalogDto.of(metadata, metadata.getYieldForLevel(1));
    }
}