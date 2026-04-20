package store.sonyk9919.api.domain.building.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;
import store.sonyk9919.api.domain.building.entity.BuildingMetadata;
import store.sonyk9919.api.domain.building.repository.BuildingMetadataRepository;

@Service
@RequiredArgsConstructor
public class BuildingMetadataQueryService {

    private final BuildingMetadataRepository metadataRepository;

    @Transactional(readOnly = true)
    public List<BuildingCatalogDto> getBuildingCatalog() {
        return metadataRepository.findAll().stream()
                .map(this::mapToCatalog)
                .collect(Collectors.toList());
    }

    private BuildingCatalogDto mapToCatalog(BuildingMetadata metadata) {
        return BuildingCatalogDto.of(metadata, metadata.getYieldForLevel(1));
    }
}