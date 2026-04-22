package store.sonyk9919.api.domain.island.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

public interface MemberIslandRepository extends JpaRepository<MemberIsland, Long> {

    Optional<MemberIsland> findByMemberAccountId(Long memberAccountId);
}