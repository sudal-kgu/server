package store.sonyk9919.api.domain.shop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.entity.Item;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopItemResponse {

    private final Long itemId;
    private final String name;
    private final int price;
    private final int maxCount;
    private final long currentCount;
    private final int unlockLevel;
    private final int expReward;
    private final boolean purchasable;

    public static ShopItemResponse of(Item item, long currentCount, boolean purchasable) {
        return new ShopItemResponse(
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
