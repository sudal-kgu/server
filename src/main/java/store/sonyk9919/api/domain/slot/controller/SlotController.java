package store.sonyk9919.api.domain.slot.controller;

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
import store.sonyk9919.api.domain.slot.service.SlotQueryService;
import store.sonyk9919.api.domain.slot.service.SlotActivateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slot")
public class SlotController {

    private final SlotQueryService slotQueryService;
    private final SlotActivateService slotActivateService;

    @GetMapping
    public List<SlotResponseDto> searchAllSlots(@AuthMember AuthMemberDto authMember) {
        return slotQueryService.getAllSlot(authMember.getId());
    }

    @GetMapping("/{slotNumber}")
    public SlotDetailResponseDto searchSlotDetail(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
        return slotQueryService.getSlotDetail(authMember.getId(), slotNumber);
    }

    @PostMapping("/activate/{slotNumber}")
    public SlotActivateResponseDto activateSlot(
            @AuthMember AuthMemberDto authMember,
            @PathVariable Integer slotNumber
    ) {
      return slotActivateService.activate(authMember.getId(), slotNumber);
    }
}