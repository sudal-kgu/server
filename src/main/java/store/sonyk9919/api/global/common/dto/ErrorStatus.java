package store.sonyk9919.api.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseResponseStatus {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "COMMON_400_1", "요청 본문 형식이 올바르지 않습니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "COMMON_400_2", "입력값이 올바르지 않습니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "COMMON_403", "접근 권한이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"COMMON_405","지원하지 않는 HTTP 메소드 입니다."),

    UNSUPPORTED_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "IMAGE_400", "지원하지 않는 이미지 파일 형식입니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE_404","request_id에 해당하는 이미지를 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
