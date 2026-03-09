package store.sonyk9919.api.global.message.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum MQStatus implements BaseResponseStatus {
    MQ_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MQ-001", "분석 요청 처리 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}