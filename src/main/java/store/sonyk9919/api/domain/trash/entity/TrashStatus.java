package store.sonyk9919.api.domain.trash.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum TrashStatus implements BaseResponseStatus {
    NOT_FOUND_TRASH("TRASH-001", HttpStatus.NOT_FOUND, "존재하지 않는 쓰레기 입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
