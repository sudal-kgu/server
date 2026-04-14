package store.sonyk9919.api.domain.slot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceSlotService;
import store.sonyk9919.api.domain.slot.dto.SlotActivateResponseDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.entity.SlotUnlockPolicy;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional
public class SlotActivateService {

    private final SlotRepository slotRepository;
    private final MemberIslandRegistryService memberIslandService;
    private final ResourceSlotService resourceSlotService;

    public SlotActivateResponseDto activate(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);

        Slot slot = slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));

        int activeCount = slotRepository.countByIslandAndActivatedTrue(island);
        int cost = SlotUnlockPolicy.costFor(activeCount);

        MemberResource updatedShell = resourceSlotService.subtractShell(island, cost);
        slot.activate();

        return new SlotActivateResponseDto(
                slot.getSlotNumber(),
                updatedShell.getAmount()
        );
    }
}