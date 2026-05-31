package store.sonyk9919.api.domain.shop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.shop.entity.GemItem;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GemItemResponse {

    private final Long gemItemId;
    private final String code;
    private final String name;
    private final String description;
    private final int gemCost;
    private final int monthlyLimit;
    private final String iconUri;

    public static GemItemResponse from(GemItem gemItem, String iconUri) {
        return new GemItemResponse(
                gemItem.getId(),
                gemItem.getCode(),
                gemItem.getName(),
                gemItem.getDescription(),
                gemItem.getGemCost(),
                gemItem.getMonthlyLimit(),
                iconUri
        );
    }
}
