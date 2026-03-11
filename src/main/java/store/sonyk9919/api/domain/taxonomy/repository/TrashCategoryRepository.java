package store.sonyk9919.api.domain.taxonomy.repository;

import org.springframework.data.repository.Repository;
import store.sonyk9919.api.domain.taxonomy.entity.TrashCategory;

public interface TrashCategoryRepository extends Repository<TrashCategory, Long> {
    int count();
    boolean existsByKo(String ko);
}
