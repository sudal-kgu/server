package store.sonyk9919.api.domain.building.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.building.dto.BuildingCatalogDto;
import store.sonyk9919.api.domain.building.dto.BuildingResponseDto;
import store.sonyk9919.api.domain.building.service.BuildingMetadataQueryService;
import store.sonyk9919.api.domain.building.service.BuildingMoveService;
import store.sonyk9919.api.domain.building.service.BuildingUpgradeService;
import store.sonyk9919.api.domain.slot.dto.SlotMoveRequestDto;
import store.sonyk9919.api.domain.slot.dto.SlotMoveResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/buildings")
public class BuildingController {

    private final BuildingMetadataQueryService queryService;
    private final BuildingUpgradeService upgradeService;
    private final BuildingMoveService moveService;


    @Operation(
            summary = "건물 카탈로그 조회",
            description = "모든 건물의 메타데이터 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "건물 목록 조회 성공")
    @GetMapping("/catalog")
    public List<BuildingCatalogDto> selectBuildMetadata() {
        return queryService.getBuildingCatalog();
    }

    @Operation(
            summary = "건물 레벨업",
            description = "해당 슬롯에 건설된 건물의 레벨을 올립니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "건물 업그레이드 성공"),
            @ApiResponse(responseCode = "400", description = "재화 부족 / 슬롯에 건물 없음 / 작동 중인 생산 건물 / 이미 최대 레벨 / 섬 레벨 부족"),
            @ApiResponse(responseCode = "404", description = "슬롯 없음 / 재화 데이터 없음"),
            @ApiResponse(responseCode = "409", description = "동시성 충돌 (락 획득 실패)")
    })
    @PostMapping("/{slotNumber}/upgrade")
    public BuildingResponseDto buildingLevelUp(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        return upgradeService.levelUp(authMember.getId(), slotNumber);
    }

    @Operation(
            summary = "건물 이동 및 스왑",
            description = "특정 슬롯에 있는 건물을 다른 슬롯으로 이동합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이동 성공"),
            @ApiResponse(responseCode = "400", description = "출발 슬롯에 건물이 없음 / 비활성화된 슬롯으로 이동 시도 / 동일한 슬롯 번호"),
            @ApiResponse(responseCode = "404", description = "슬롯 없음"),
            @ApiResponse(responseCode = "409", description = "동시성 충돌 (락 획득 실패)")
    })
    @PostMapping("/move")
    public SlotMoveResponseDto move(@AuthMember AuthMemberDto authMember, @Valid @RequestBody SlotMoveRequestDto slotMoveRequestDto) {
        return moveService.moveOf(authMember.getId(), slotMoveRequestDto);
    }
}