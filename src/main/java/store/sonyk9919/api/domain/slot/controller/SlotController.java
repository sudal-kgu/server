package store.sonyk9919.api.domain.slot.controller;

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
import store.sonyk9919.api.domain.slot.dto.SlotActivateResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotDetailResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotListResponseDto;
import store.sonyk9919.api.domain.slot.service.SlotActivateService;
import store.sonyk9919.api.domain.slot.service.SlotQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/slots")
public class SlotController {

    private final SlotQueryService slotQueryService;
    private final SlotActivateService slotActivateService;

    @Operation(
            summary = "유저의 전체 슬롯 조회",
            description = "현재 로그인된 유저의 섬에 있는 모든 슬롯의 상태와 배치된 건물 기본 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "슬롯 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "섬을 찾을 수 없음 (ISLAND-003)")
    })
    @GetMapping
    public SlotListResponseDto searchAllSlots(@AuthMember AuthMemberDto authMember) {
        return slotQueryService.getAllSlot(authMember.getId());
    }

    @Operation(
            summary = "특정 슬롯 상세 조회",
            description = "선택한 슬롯 번호에 대한 상세 정보(생산량, 남은 시간, 효과 등)를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "슬롯 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 슬롯 번호 (SLOT-004)"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "섬을 찾을 수 없음 (ISLAND-003)")
    })
    @GetMapping("/{slotNumber}")
    public SlotDetailResponseDto searchSlotDetail(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        return slotQueryService.getSlotDetail(authMember.getId(), slotNumber);
    }

    @Operation(
            summary = "슬롯 활성화",
            description = "현재 레벨과 활성화된 슬롯 개수에 따라 필요 조개를 차감하고 해당 슬롯을 활성화합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "슬롯 활성화 성공, 차감 후 남은 조개 수량 반환"),
            @ApiResponse(responseCode = "400", description = "이미 활성화된 슬롯 (SLOT-001), 최대 활성화 개수 초과 (SLOT-005), 조개 잔액 부족 (ISLAND-002)"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 슬롯 번호 (SLOT-004), 섬을 찾을 수 없음 (ISLAND-003)")
    })
    @PostMapping("/{slotNumber}/activate")
    public SlotActivateResponseDto activateSlot(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
      return slotActivateService.activate(authMember.getId(), slotNumber);
    }
}