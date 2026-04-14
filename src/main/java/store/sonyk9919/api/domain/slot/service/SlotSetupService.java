package store.sonyk9919.api.domain.slot.service;

import static store.sonyk9919.api.domain.slot.entity.SlotUnlockPolicy.MAX_SLOT_COUNT;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.slot.entity.Slot;
import store.sonyk9919.api.domain.slot.repository.SlotRepository;

@Service
@RequiredArgsConstructor
public class SlotSetupService {

    private final SlotRepository slotRepository;

    public void setupSlots(MemberIsland island) {
        List<Slot> initialSlots = IntStream.rangeClosed(1, MAX_SLOT_COUNT)
                .mapToObj(i -> Slot.of(island, i))
                .collect(Collectors.toList());

        slotRepository.saveAll(initialSlots);
    }
}