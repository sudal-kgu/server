package store.sonyk9919.api.domain.island.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

import java.util.Optional;

public interface MemberIslandRepository extends JpaRepository<MemberIsland, Long> {

    Optional<MemberIsland> findByMemberAccountId(Long memberAccountId);
}