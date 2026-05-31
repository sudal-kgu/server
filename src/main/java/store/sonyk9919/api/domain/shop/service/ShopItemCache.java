package store.sonyk9919.api.domain.shop.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.shop.entity.ShopItem;
import store.sonyk9919.api.domain.shop.repository.ShopItemRepository;

@Component
@RequiredArgsConstructor
public class ShopItemCache {

    private final ShopItemRepository shopItemRepository;

    @Cacheable(cacheNames = "shop-items", key = "'all'")
    public List<ShopItem> getAll() {
        return shopItemRepository.findAllByOrderByIdAsc();
    }
}
