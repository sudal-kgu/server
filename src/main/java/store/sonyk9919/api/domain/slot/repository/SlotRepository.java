package store.sonyk9919.api.domain.slot.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.slot.entity.Slot;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @EntityGraph(attributePaths = {"building", "building.buildingMetadata", "building.buildingMetadata.yields"})
    List<Slot> findAllByIsland(MemberIsland island);

    @EntityGraph(attributePaths = {"building", "building.buildingMetadata"})
    Optional<Slot> findByIslandAndSlotNumber(MemberIsland island, Integer slotNumber);

    int countByIslandAndActivatedTrue(MemberIsland island);
}