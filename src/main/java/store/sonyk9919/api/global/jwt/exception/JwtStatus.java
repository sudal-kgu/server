package store.sonyk9919.api.global.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;


@Getter
@RequiredArgsConstructor
public enum JwtStatus implements BaseResponseStatus {
    JWT_EXPIRY_REQUEST(HttpStatus.UNAUTHORIZED, "JWT-001", "만료된 요청입니다."),
    JWT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "JWT-002", "유효하지 않은 요청입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
