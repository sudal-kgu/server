package store.sonyk9919.api.domain.island.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum IslandStatus implements BaseResponseStatus {
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "ISLAND-001", "amount는 0보다 커야 합니다."),
    INSUFFICIENT_AMOUNT(HttpStatus.BAD_REQUEST, "ISLAND-002", "보유 자원이 부족합니다."),
    NOT_FOUND_ISLAND(HttpStatus.NOT_FOUND, "ISLAND-003", "섬을 찾을 수 없습니다."),
    LEVEL_SPEC_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "ISLAND-004", "레벨 스펙을 찾을 수 없습니다."),
    ALREADY_MAX_LEVEL(HttpStatus.BAD_REQUEST, "ISLAND-005", "이미 최대 레벨입니다."),
    LEVEL_TOO_LOW(HttpStatus.BAD_REQUEST, "ISLAND-006", "섬의 요구 레벨이 충족되지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
