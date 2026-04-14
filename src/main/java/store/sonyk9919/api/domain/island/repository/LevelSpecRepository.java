package store.sonyk9919.api.domain.island.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.LevelSpec;

public interface LevelSpecRepository extends JpaRepository<LevelSpec, Integer> {
    LevelSpec findByLevel(int level);
}