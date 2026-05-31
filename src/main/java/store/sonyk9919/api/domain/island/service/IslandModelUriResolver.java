package store.sonyk9919.api.domain.island.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.island.entity.IslandItemUsage;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.repository.IslandItemUsageRepository;
import store.sonyk9919.api.domain.shop.service.ShopItemCache;

@Component
@RequiredArgsConstructor
public class IslandModelUriResolver {

    private static final String SOIL_PURIFICATION_CODE = "SOIL_PURIFICATION";

    private final IslandItemUsageRepository islandItemUsageRepository;
    private final ShopItemCache shopItemCache;

    @Value("${island.model.base-url}")
    private String modelBaseUrl;

    public String resolve(MemberIsland island) {
        int modelIndex = Math.min(island.getLevel(), 2) + (int) getSoilPurificationCount(island);
        return modelBaseUrl + "/island_" + modelIndex + ".glb";
    }

    private long getSoilPurificationCount(MemberIsland island) {
        return shopItemCache.getAll().stream()
                .filter(item -> SOIL_PURIFICATION_CODE.equals(item.getCode()))
                .findFirst()
                .flatMap(item -> islandItemUsageRepository.findByIslandAndItem(island, item))
                .map(IslandItemUsage::getUseCount)
                .orElse(0L);
    }
}
