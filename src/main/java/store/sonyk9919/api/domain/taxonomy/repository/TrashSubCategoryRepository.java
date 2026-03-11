package store.sonyk9919.api.domain.taxonomy.repository;

import org.springframework.data.repository.Repository;
import store.sonyk9919.api.domain.taxonomy.entity.TrashSubCategory;

public interface TrashSubCategoryRepository extends Repository<TrashSubCategory, Long> {
    int count();
}
