package store.sonyk9919.api.domain.building.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.entity.AuthMember;
import store.sonyk9919.api.domain.building.dto.BuildingLayoutDto;
import store.sonyk9919.api.domain.building.dto.BuildingResponseDto;
import store.sonyk9919.api.domain.building.service.BuildingLayoutService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/slots/{slotNumber}/buildings")
public class BuildingLayoutController {

    private final BuildingLayoutService layoutService;

    @Operation(
            summary = "건물 배치",
            description = "특정 슬롯에 재화로 새로운 건물을 배치합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배치 성공"),
            @ApiResponse(responseCode = "400", description = "재화 부족 / 슬롯에 건물이 이미 존재"),
            @ApiResponse(responseCode = "404", description = "슬롯 없음 / 건물 메타데이터 없음 / 재화 데이터 없음"),
            @ApiResponse(responseCode = "409", description = "동시성 충돌 (락 획득 실패)")
    })
    @PostMapping
    public BuildingResponseDto build(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber,
            @RequestBody BuildingLayoutDto requestDto
    ) {
        return layoutService.buildOf(authMember.getId(), slotNumber, requestDto);
    }

    @Operation(
            summary = "건물 철거",
            description = "특정 슬롯의 건물을 철거하고 일부 재화를 환불받습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "철거 성공"),
            @ApiResponse(responseCode = "400", description = "슬롯에 건물 없음"),
            @ApiResponse(responseCode = "404", description = "슬롯 없음 / 재화 데이터 없음"),
            @ApiResponse(responseCode = "409", description = "동시성 충돌 (락 획득 실패)")
    })
    @DeleteMapping
    public BuildingResponseDto demolish(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        return layoutService.demolishOf(authMember.getId(), slotNumber);
    }
}