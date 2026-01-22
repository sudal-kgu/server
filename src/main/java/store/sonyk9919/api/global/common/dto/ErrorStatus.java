package store.sonyk9919.api.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseResponseStatus {

    // 4xx
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "VALIDATION_FAIL", "입력값이 올바르지 않습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "INVALID_REQUEST_BODY", "요청 본문 형식이 올바르지 않습니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "NO_PERMISSION", "해당 리소스에 접근할 권한이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"METHOD_NOT_ALLOWED","지원하지 않는 HTTP 메소드 입니다."),

    // 5xx
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", "데이터베이스 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
