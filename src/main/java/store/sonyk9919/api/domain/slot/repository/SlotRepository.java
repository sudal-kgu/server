package store.sonyk9919.api.domain.slot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.slot.entity.Slot;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findAllByIsland(MemberIsland island);
    Optional<Slot> findByIslandAndSlotNumber(MemberIsland island, Integer slotNumber);
}