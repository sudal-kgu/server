package store.sonyk9919.api.domain.taxonomy.repository;

import org.springframework.data.repository.Repository;
import store.sonyk9919.api.domain.taxonomy.entity.TrashCategory;

import java.util.Optional;

public interface TrashCategoryRepository extends Repository<TrashCategory, Long> {
    Optional<TrashCategory> findById(Long id);
    int count();
    boolean existsByKo(String ko);
}
