package store.sonyk9919.api.domain.shop.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.global.common.lock.DistributedLock;
import store.sonyk9919.api.domain.island.entity.IslandItemUsage;
import store.sonyk9919.api.domain.island.entity.Item;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.entity.MemberResource;
import store.sonyk9919.api.domain.island.entity.ResourceType;
import store.sonyk9919.api.domain.island.repository.IslandItemUsageRepository;
import store.sonyk9919.api.domain.island.repository.ItemRepository;
import store.sonyk9919.api.domain.island.service.IslandLevelService;
import store.sonyk9919.api.domain.island.service.ItemCache;
import store.sonyk9919.api.domain.island.service.MemberIslandRegistryService;
import store.sonyk9919.api.domain.island.service.ResourceService;
import store.sonyk9919.api.domain.shop.dto.ShopItemResponse;
import store.sonyk9919.api.domain.shop.dto.ShopPurchaseResponse;
import store.sonyk9919.api.domain.shop.exception.ShopStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ItemCache itemCache;
    private final ItemRepository itemRepository;
    private final IslandItemUsageRepository islandItemUsageRepository;
    private final MemberIslandRegistryService memberIslandRegistryService;
    private final ResourceService resourceService;
    private final IslandLevelService islandLevelService;

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

    @DistributedLock(keys = {"'island:' + #memberId + ':exp'", "'island:' + #memberId + ':shell'"})
    @Transactional
    public ShopPurchaseResponse purchaseItem(Long memberId, Long itemId) {
        MemberIsland island = memberIslandRegistryService.getIsland(memberId);
        Item item = getValidatedItem(itemId, island.getLevel());
        IslandItemUsage usage = getOrCreateUsage(island, item);
        return applyPurchase(island, item, usage);
    }

    private Item getValidatedItem(Long itemId, int islandLevel) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ShopStatus.ITEM_NOT_FOUND));
        if (islandLevel < item.getUnlockLevel()) {
            throw new CustomException(ShopStatus.ITEM_LOCKED);
        }
        return item;
    }

    private IslandItemUsage getOrCreateUsage(MemberIsland island, Item item) {
        IslandItemUsage usage = islandItemUsageRepository.findByIslandAndItem(island, item)
                .orElseGet(() -> islandItemUsageRepository.save(IslandItemUsage.create(item, island)));
        if (usage.getUseCount() >= item.getMaxCount()) {
            throw new CustomException(ShopStatus.ITEM_PURCHASE_LIMIT_EXCEEDED);
        }
        return usage;
    }

    private ShopPurchaseResponse applyPurchase(MemberIsland island, Item item, IslandItemUsage usage) {
        MemberResource updatedShell = resourceService.subtract(island, ResourceType.SHELL, item.getPrice());
        usage.incrementUseCount();
        islandLevelService.addItemExp(island, item.getExpReward());
        return ShopPurchaseResponse.of(item, usage, updatedShell.getAmount());
    }
}
