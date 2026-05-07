package store.sonyk9919.api.domain.shop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.entity.IslandItemUsage;
import store.sonyk9919.api.domain.island.entity.Item;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopPurchaseResponse {

    private final Long itemId;
    private final String itemName;
    private final long remainingShell;
    private final long currentCount;
    private final int expReward;
    private final LevelUpResult levelUpResult;

    public static ShopPurchaseResponse of(Item item, IslandItemUsage usage, long remainingShell, LevelUpResult levelUpResult) {
        return new ShopPurchaseResponse(
                item.getId(),
                item.getName(),
                remainingShell,
                usage.getUseCount(),
                item.getExpReward(),
                levelUpResult
        );
    }
}
