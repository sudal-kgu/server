package store.sonyk9919.api.domain.island.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;

public interface MemberResourceRepository extends JpaRepository<MemberResource, Long> {
    MemberResource findByIslandAndResourceType(MemberIsland island, ResourceType type);
}