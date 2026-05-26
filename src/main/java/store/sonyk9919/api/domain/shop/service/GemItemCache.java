package store.sonyk9919.api.domain.shop.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.shop.entity.GemItem;
import store.sonyk9919.api.domain.shop.repository.GemItemRepository;

@Component
@RequiredArgsConstructor
public class GemItemCache {

    private final GemItemRepository gemItemRepository;

    @Cacheable(cacheNames = "gem-items", key = "'all'")
    public List<GemItem> getAll() {
        return gemItemRepository.findAllByOrderByIdAsc();
    }
}
