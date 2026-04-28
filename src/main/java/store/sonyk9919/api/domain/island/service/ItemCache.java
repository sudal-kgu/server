package store.sonyk9919.api.domain.island.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.island.entity.Item;
import store.sonyk9919.api.domain.island.repository.ItemRepository;

@Component
@RequiredArgsConstructor
public class ItemCache {

    private final ItemRepository itemRepository;

    @Cacheable(cacheNames = "items", key = "'all'")
    public List<Item> getAll() {
        return itemRepository.findAllByOrderByIdAsc();
    }
}
