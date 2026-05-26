package store.sonyk9919.api.domain.shop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum ShopStatus implements BaseResponseStatus {
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "SHOP-001", "존재하지 않는 아이템입니다."),
    ITEM_LOCKED(HttpStatus.BAD_REQUEST, "SHOP-002", "해금 레벨에 도달하지 않았습니다."),
    ITEM_PURCHASE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "SHOP-003", "최대 구매 횟수를 초과했습니다."),
    GEM_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "SHOP-004", "존재하지 않는 보석 교환 아이템입니다."),
    GEM_EXCHANGE_MONTHLY_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "SHOP-005", "이번 달 교환 한도를 초과했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
