package store.sonyk9919.api.domain.island.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;

public interface MemberResourceRepository extends JpaRepository<MemberResource, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MemberResource> findWithLockByIslandAndResourceType(MemberIsland island, ResourceType type);

    List<MemberResource> findAllByIslandMemberAccountId(Long memberAccountId);

    Optional<MemberResource> findByIslandAndResourceType(MemberIsland island, ResourceType type);
}