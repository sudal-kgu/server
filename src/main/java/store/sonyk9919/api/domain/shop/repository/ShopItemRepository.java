package store.sonyk9919.api.domain.shop.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.sonyk9919.api.domain.shop.entity.ShopItem;

public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {

    List<ShopItem> findAllByOrderByIdAsc();
}
