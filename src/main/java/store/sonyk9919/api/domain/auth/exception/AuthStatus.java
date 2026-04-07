package store.sonyk9919.api.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum AuthStatus implements BaseResponseStatus {
    NOT_SUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH-001", "지원하지 않는 제공자 입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "AUTH-002", "잘 못 된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-003", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
