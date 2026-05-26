package store.sonyk9919.api.domain.shop.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.shop.entity.GemItem;

public interface GemItemRepository extends JpaRepository<GemItem, Long> {

    List<GemItem> findAllByOrderByIdAsc();
}
