package store.sonyk9919.api.domain.shop.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.entity.IslandItemUsage;
import store.sonyk9919.api.domain.island.entity.Item;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.repository.IslandItemUsageRepository;
import store.sonyk9919.api.domain.island.service.ItemCache;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.shop.dto.ShopItemResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ItemCache itemCache;
    private final IslandItemUsageRepository islandItemUsageRepository;
    private final MemberIslandRegistryService memberIslandRegistryService;

    public List<ShopItemResponse> getItems(Long memberId) {
        MemberIsland island = memberIslandRegistryService.getIsland(memberId);

        Map<Long, Long> usageByItemId = islandItemUsageRepository.findAllByIsland(island).stream()
                .collect(Collectors.toMap(u -> u.getItem().getId(), IslandItemUsage::getUseCount));

        List<Item> items = itemCache.getAll();

        return items.stream()
                .map(item -> {
                    long currentCount = usageByItemId.getOrDefault(item.getId(), 0L);
                    boolean purchasable = island.getLevel() >= item.getUnlockLevel()
                            && currentCount < item.getMaxCount();
                    return ShopItemResponse.of(item, currentCount, purchasable);
                })
                .toList();
    }
}
