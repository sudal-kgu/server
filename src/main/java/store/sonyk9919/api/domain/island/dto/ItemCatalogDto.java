package store.sonyk9919.api.domain.island.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.entity.Item;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemCatalogDto {

    private final Long itemId;
    private final String name;
    private final int price;
    private final int maxCount;
    private final long currentCount;
    private final int unlockLevel;
    private final int expReward;
    private final boolean purchasable;

    public static ItemCatalogDto from(Item item, long currentCount, boolean purchasable) {
        return new ItemCatalogDto(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getMaxCount(),
                currentCount,
                item.getUnlockLevel(),
                item.getExpReward(),
                purchasable
        );
    }
}
