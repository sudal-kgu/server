package store.sonyk9919.api.domain.island.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum ResourceStatus implements BaseResponseStatus {
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE-001", "존재하지 않는 재화입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}