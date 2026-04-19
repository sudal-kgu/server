package store.sonyk9919.api.global.common.lock;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum LockStatus implements BaseResponseStatus {
    LOCK_ACQUISITION_FAILED(HttpStatus.CONFLICT, "LOCK-001", "현재 요청이 많습니다. 잠시 후 다시 시도해 주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
