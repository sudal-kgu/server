package store.sonyk9919.api.domain.trash.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum TrashStatus implements BaseResponseStatus {
    NOT_FOUND_TRASH(HttpStatus.NOT_FOUND, "TRASH-001", "존재하지 않는 쓰레기 입니다."),
    ALREADY_CONFIRMED_TRASH(HttpStatus.CONFLICT, "TRASH-002", "이미 확정 처리된 쓰레기입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
