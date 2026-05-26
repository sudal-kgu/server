package store.sonyk9919.api.domain.shop.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import store.sonyk9919.api.domain.shop.entity.GemItem;
import store.sonyk9919.api.domain.shop.entity.MemberGemExchange;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GemExchangeResponse {

    private final Long gemItemId;
    private final String gemItemName;
    private final LocalDateTime exchangedAt;
    private final long remainingGem;

    public static GemExchangeResponse of(MemberGemExchange exchange, GemItem gemItem, long remainingGem) {
        return new GemExchangeResponse(
                gemItem.getId(),
                gemItem.getName(),
                exchange.getCreatedAt(),
                remainingGem
        );
    }
}
