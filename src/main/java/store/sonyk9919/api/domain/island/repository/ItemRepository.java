package store.sonyk9919.api.domain.island.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.island.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOrderByIdAsc();
}
