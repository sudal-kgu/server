package store.sonyk9919.api.domain.trash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.trash.entity.TrashCategory;

public interface TrashCategoryRepository extends JpaRepository<TrashCategory, Long> {
}
