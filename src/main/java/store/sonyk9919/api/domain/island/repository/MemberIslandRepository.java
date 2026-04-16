package store.sonyk9919.api.domain.island.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import store.sonyk9919.api.domain.island.entity.MemberIsland;

public interface MemberIslandRepository extends JpaRepository<MemberIsland, Long> {

    Optional<MemberIsland> findByMemberAccountId(Long MemberAccountId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MemberIsland> findWithWriteLockByMemberAccountId(Long memberAccountId);
}