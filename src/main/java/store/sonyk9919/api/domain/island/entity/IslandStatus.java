package store.sonyk9919.api.domain.island.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum IslandStatus implements BaseResponseStatus {
    NOT_FOUND_ISLAND(HttpStatus.NOT_FOUND, "ISLAND-001", "섬을 찾을 수 없습니다."),
    ISLAND_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "ISLAND-002", "이미 섬이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
