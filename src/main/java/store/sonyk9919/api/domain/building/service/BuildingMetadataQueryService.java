package store.sonyk9919.api.domain.building.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;

@Service
@RequiredArgsConstructor
public class BuildingMetadataQueryService {

    private final BuildingMetadataCache metadataCache;

    @Transactional(readOnly = true)
    public List<BuildingCatalogDto> getBuildingCatalog() {
        return metadataCache.get();
    }
}