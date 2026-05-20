package store.sonyk9919.api.domain.island.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.Region;

public interface MemberIslandRepository extends JpaRepository<MemberIsland, Long> {

    Optional<MemberIsland> findByMemberAccountId(Long memberAccountId);

    Page<MemberIsland> findAllByOrderByCumulativeExpDescIdAsc(Pageable pageable);

    Page<MemberIsland> findAllByRegionOrderByCumulativeExpDescIdAsc(Region region, Pageable pageable);

    @Query("SELECT COUNT(i) FROM MemberIsland i WHERE i.cumulativeExp > :exp OR (i.cumulativeExp = :exp AND i.id < :id)")
    int countRankingsBefore(int exp, Long id);

    @Query("SELECT COUNT(i) FROM MemberIsland i WHERE i.region = :region AND (i.cumulativeExp > :exp OR (i.cumulativeExp = :exp AND i.id < :id))")
    int countRegionalRankingsBefore(Region region, int exp, Long id);
}