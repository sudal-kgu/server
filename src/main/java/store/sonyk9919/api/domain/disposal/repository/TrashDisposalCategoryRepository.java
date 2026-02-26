package store.sonyk9919.api.domain.disposal.repository;

import org.springframework.data.repository.Repository;
import store.sonyk9919.api.domain.disposal.entity.TrashDisposalCategory;

public interface TrashDisposalCategoryRepository extends Repository<TrashDisposalCategory, Long> {
    int count();
}
