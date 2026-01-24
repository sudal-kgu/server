package store.sonyk9919.api.domain.trash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.trash.entity.Trash;

public interface TrashRepository extends JpaRepository<Trash, Long> {
}
