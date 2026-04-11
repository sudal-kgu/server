package store.sonyk9919.api.domain.island.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

import java.util.Optional;

public interface MemberIslandRepository extends JpaRepository<MemberIsland, Long> {

    Optional<MemberIsland> findByMemberAccountId(Long MemberAccountId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MemberIsland> findWithWriteLockByMemberAccountId(Long memberAccountId);
}
