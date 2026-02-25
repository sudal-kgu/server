package store.sonyk9919.api.domain.sse.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum SseStatus implements BaseResponseStatus {
    SSE_DUPLICATE_ATTEMPT(HttpStatus.CONFLICT, "SSE-001", "이미 구독 중인 요청입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
