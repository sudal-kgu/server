package store.sonyk9919.api.domain.shop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.island.dto.LevelUpResult;
import store.sonyk9919.api.domain.island.dto.MemberIslandDto;
import store.sonyk9919.api.domain.island.entity.IslandItemUsage;
import store.sonyk9919.api.domain.island.entity.Item;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ShopPurchaseResponse {

    private final Purchased purchased;
    private final MemberIslandDto island;
    private final LevelUpResult.UnlockNotice notice;

    public static ShopPurchaseResponse of(Item item, IslandItemUsage usage, long remainingShell, int cumulativeExp, LevelUpResult levelUpResult) {
        return new ShopPurchaseResponse(
                new Purchased(item.getId(), item.getName(), cumulativeExp, usage.getUseCount(), remainingShell),
                levelUpResult.getIsland(),
                levelUpResult.getNotice()
        );
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Purchased {
        private final Long itemId;
        private final String itemName;
        private final int cumulativeExp;
        private final long currentCount;
        private final long remainingShell;
    }
}
