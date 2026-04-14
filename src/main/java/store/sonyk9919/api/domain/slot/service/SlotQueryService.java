package store.sonyk9919.api.domain.slot.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.slot.dto.SlotDetailResponseDto;
import store.sonyk9919.api.domain.slot.dto.SlotResponseDto;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.exception.SlotStatus;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlotQueryService {

    private final SlotRepository slotRepository;
    private final MemberIslandRegistryService memberIslandService;

    public List<SlotResponseDto> getAllSlot(Long memberId) {
        MemberIsland island = memberIslandService.getIsland(memberId);

        List<Slot> slots = slotRepository.findAllByIsland(island);
        return slots.stream()
                .map(SlotResponseDto::from)
                .collect(Collectors.toList());
    }

    public SlotDetailResponseDto getSlotDetail(Long memberId, Integer slotNumber) {
        MemberIsland island = memberIslandService.getIsland(memberId);

        Slot slot = slotRepository.findByIslandAndSlotNumber(island, slotNumber)
                .orElseThrow(() -> new CustomException(SlotStatus.SLOT_NOT_FOUND));
        return SlotDetailResponseDto.from(slot);
    }
}