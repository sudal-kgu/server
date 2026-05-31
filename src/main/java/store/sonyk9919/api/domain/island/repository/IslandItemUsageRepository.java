package store.sonyk9919.api.domain.island.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import store.sonyk9919.api.domain.island.entity.IslandItemUsage;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.shop.entity.ShopItem;

public interface IslandItemUsageRepository extends JpaRepository<IslandItemUsage, Long> {

    Optional<IslandItemUsage> findByIslandAndItem(MemberIsland island, ShopItem item);

    List<IslandItemUsage> findAllByIsland(MemberIsland island);

    @Query("SELECT u.item.id, u.useCount FROM IslandItemUsage u WHERE u.island = :island")
    List<Object[]> findItemIdAndUseCountByIsland(@Param("island") MemberIsland island);
}
