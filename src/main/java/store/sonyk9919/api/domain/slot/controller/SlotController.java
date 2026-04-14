package store.sonyk9919.api.domain.slot.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
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
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.service.SlotActivateService;
import store.sonyk9919.api.domain.slot.service.SlotQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slot")
public class SlotController {

    private final SlotQueryService slotQueryService;
    private final SlotActivateService slotActivateService;

    @Operation(
            summary = "유저의 전체 슬롯 조회",
            description = "현재 로그인된 유저의 섬에 있는 모든 슬롯의 상태와 배치된 건물 기본 정보를 조회합니다."
    )
    @GetMapping
    public List<SlotResponseDto> searchAllSlots(@AuthMember AuthMemberDto authMember) {
        return slotQueryService.getAllSlot(authMember.getId());
    }

    @Operation(
            summary = "특정 슬롯 상세 조회",
            description = "선택한 슬롯 번호에 대한 상세 정보(생산량, 남은 시간, 효과 등)를 조회합니다."
    )
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
    @PostMapping("/activate/{slotNumber}")
    public SlotActivateResponseDto activateSlot(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
      return slotActivateService.activate(authMember.getId(), slotNumber);
    }
}