package store.sonyk9919.api.domain.building.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.building.dto.HarvestResponseDto;
import store.sonyk9919.api.domain.building.dto.OperationPreviewDto;
import store.sonyk9919.api.domain.building.service.BuildingHarvestService;
import store.sonyk9919.api.domain.building.service.BuildingOperateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/buildings/operations")
public class BuildingHarvestController {

    private final BuildingHarvestService harvestService;
    private final BuildingOperateService operateService;

    @Operation(
            summary = "건물 연료 주입",
            description = "특정 슬롯의 건물에 연료를 투입해 보석을 생산합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가동 성공"),
            @ApiResponse(responseCode = "400", description = "생산 시설 아님 / 이미 가동 중 / 연료 부족"),
            @ApiResponse(responseCode = "404", description = "슬롯 없음 / 재화 데이터 없음"),
            @ApiResponse(responseCode = "409", description = "동시성 충돌 (락 획득 실패)")
    })
    @PostMapping("/{slotNumber}")
    public void injectFuel(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        operateService.operate(authMember.getId(), slotNumber);
    }

    @Operation(
            summary = "단일 건물 수확",
            description = "특정 슬롯의 건물에서 지금까지 생산된 보석을 수확합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "단일 수확 성공"),
            @ApiResponse(responseCode = "400", description = "미가동 건물 / 수확 시간 역전 / 수확량 0"),
            @ApiResponse(responseCode = "404", description = "슬롯 없음 / 재화 데이터 없음"),
            @ApiResponse(responseCode = "409", description = "동시성 충돌 (락 획득 실패)")
    })
    @PostMapping("/{slotNumber}/harvest")
    public int harvestBuilding(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        return harvestService.harvest(authMember.getId(), slotNumber);
    }

    @Operation(
            summary = "섬 전체 건물 일괄 수확",
            description = "유저의 섬에 있는 모든 생산 가능 건물의 보석을 한 번에 수확합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일괄 수확 성공"),
            @ApiResponse(responseCode = "400", description = "수확량 0"),
            @ApiResponse(responseCode = "404", description = "섬 정보 없음 / 재화 데이터 없음"),
            @ApiResponse(responseCode = "409", description = "동시성 충돌 (락 획득 실패)")
    })
    @PostMapping("/harvests")
    public int harvestBuildings(
            @AuthMember AuthMemberDto authMember
    ) {
        return harvestService.harvestAll(authMember.getId());
    }

    @Operation(
            summary = "건물 가동 연료 소모량 미리보기",
            description = "특정 슬롯의 건물을 가동할 때 필요한 연료량을 미리 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "슬롯 없음 / 건물 없음")
    })
    @GetMapping("/{slotNumber}")
    public OperationPreviewDto previewFuel(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        return operateService.preview(authMember.getId(), slotNumber);
    }

    @Operation(
            summary = "생산 건물 수확량 미리보기",
            description = "해당 슬롯의 생산 시설에서 현재 수확 가능한 보석량을 미리 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "슬롯 없음 / 건물 없음")
    })
    @GetMapping("/{slotNumber}/harvest")
    public HarvestResponseDto previewHarvest(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        return harvestService.preview(authMember.getId(), slotNumber);
    }
}