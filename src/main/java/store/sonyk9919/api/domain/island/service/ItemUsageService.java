package store.sonyk9919.api.domain.island.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.sonyk9919.api.domain.island.dto.ItemCatalogDto;
import store.sonyk9919.api.domain.island.entity.Item;
import store.sonyk9919.api.domain.island.entity.MemberIsland;
import store.sonyk9919.api.domain.island.exception.IslandStatus;
import store.sonyk9919.api.domain.island.repository.IslandItemUsageRepository;
import store.sonyk9919.api.domain.island.repository.MemberIslandRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemUsageService {

    private final MemberIslandRepository memberIslandRepository;
    private final IslandItemUsageRepository islandItemUsageRepository;
    private final ItemCache itemCache;

    public List<ItemCatalogDto> getItemUsages(Long memberAccountId) {
        MemberIsland island = memberIslandRepository.findByMemberAccountId(memberAccountId)
                .orElseThrow(() -> new CustomException(IslandStatus.NOT_FOUND_ISLAND));
        List<Item> items = itemCache.getAll();
        Map<Long, Long> usageByItemId = buildUsageMap(island);
        return items.stream()
                .map(item -> toItemCatalogDto(item, usageByItemId, island))
                .collect(Collectors.toList());
    }

    private Map<Long, Long> buildUsageMap(MemberIsland island) {
        return islandItemUsageRepository.findItemIdAndUseCountByIsland(island).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
    }

    private ItemCatalogDto toItemCatalogDto(Item item, Map<Long, Long> usageByItemId, MemberIsland island) {
        long currentCount = usageByItemId.getOrDefault(item.getId(), 0L);
        boolean purchasable = island.getLevel() >= item.getUnlockLevel()
                && currentCount < item.getMaxCount();
        return ItemCatalogDto.from(item, currentCount, purchasable);
    }
}
