package store.sonyk9919.api.domain.building.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;
import store.sonyk9919.api.domain.building.service.BuildingMetadataQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/buildings")
public class BuildingController {

    private final BuildingMetadataQueryService queryService;

    @GetMapping("/catalog")
    public List<BuildingCatalogDto> selectBuildMetadata() {
        return queryService.getBuildingCatalog();
    }
}