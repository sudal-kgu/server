package store.sonyk9919.api.domain.island.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.IslandItemUsage;
import store.sonyk9919.api.domain.island.entity.Item;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

public interface IslandItemUsageRepository extends JpaRepository<IslandItemUsage, Long> {

    Optional<IslandItemUsage> findByIslandAndItem(MemberIsland island, Item item);

    List<IslandItemUsage> findAllByIsland(MemberIsland island);
}
