package store.sonyk9919.api.domain.building.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;
import store.sonyk9919.api.domain.building.dto.BuildingLayoutDto;
import store.sonyk9919.api.domain.building.service.BuildingLayoutService;
import store.sonyk9919.api.domain.building.service.BuildingMetadataQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/buildings")
public class BuildingController {

    private final BuildingMetadataQueryService queryService;
    private final BuildingLayoutService buildingLayoutService;

    @GetMapping("/catalog")
    public List<BuildingCatalogDto> selectBuildMetadata() {
        return queryService.getBuildingCatalog();
    }

    @PostMapping("/{slotNumber}")
    public void build(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber,
            @RequestBody BuildingLayoutDto requestDto
    ) {
        buildingLayoutService.buildOf(authMember.getId(), slotNumber, requestDto);
    }

    @DeleteMapping("/{slotNumber}")
    public void demolish(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        buildingLayoutService.demolishOf(authMember.getId(), slotNumber);
    }
}