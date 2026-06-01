package store.sonyk9919.api.domain.island.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.shop.entity.ShopItem;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemUsageDto {

    private final Long itemId;
    private final String name;
    private final int price;
    private final int maxCount;
    private final long currentCount;
    private final int unlockLevel;
    private final int expReward;
    private final boolean purchasable;
    private final List<String> modelUri;

    public static ItemUsageDto from(
            ShopItem item, long currentCount, boolean purchasable, List<String> modelUri
    ) {
        return new ItemUsageDto(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getMaxCount(),
                currentCount,
                item.getUnlockLevel(),
                item.getExpReward(),
                purchasable,
                modelUri
        );
    }
}
