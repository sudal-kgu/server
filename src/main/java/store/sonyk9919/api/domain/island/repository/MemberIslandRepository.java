package store.sonyk9919.api.domain.island.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.Region;

public interface MemberIslandRepository extends JpaRepository<MemberIsland, Long> {

    Optional<MemberIsland> findByMemberAccountId(Long memberAccountId);

    Page<MemberIsland> findAllByOrderByCumulativeExpDesc(Pageable pageable);

    Page<MemberIsland> findAllByRegionOrderByCumulativeExpDesc(Region region, Pageable pageable);

    int countByCumulativeExpGreaterThan(int cumulativeExp);

    int countByRegionAndCumulativeExpGreaterThan(Region region, int cumulativeExp);
}