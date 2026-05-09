package store.sonyk9919.api.domain.building.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "건물 카탈로그 조회",
            description = "모든 건물의 메타데이터 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "건물 목록 조회 성공")
    @GetMapping("/catalog")
    public List<BuildingCatalogDto> selectBuildMetadata() {
        return queryService.getBuildingCatalog();
    }
}