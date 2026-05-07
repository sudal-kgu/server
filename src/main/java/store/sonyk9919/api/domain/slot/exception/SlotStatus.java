package store.sonyk9919.api.domain.slot.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum SlotStatus implements BaseResponseStatus{
    SLOT_ALREADY_ACTIVATED(HttpStatus.BAD_REQUEST, "SLOT-001", "이미 활성화된 슬롯입니다."),
    SLOT_NOT_ACTIVATED(HttpStatus.BAD_REQUEST, "SLOT-002", "비활성화된 슬롯에는 건물을 지을 수 없습니다."),
    SLOT_ALREADY_BUILT(HttpStatus.BAD_REQUEST, "SLOT-003", "이미 건물이 배치된 슬롯입니다."),
    SLOT_NOT_FOUND(HttpStatus.BAD_REQUEST, "SLOT-004", "존재하지 않는 슬롯입니다."),

    EXCEED_MAX_SLOT_FOR_LEVEL(HttpStatus.BAD_REQUEST, "SLOT-005", "현재 레벨에서 최대 활성화 개수를 초과했습니다."),
    SLOT_EMPTY(HttpStatus.BAD_REQUEST, "SLOT-006", "빈 슬롯입니다. (건물이 설치 되지 않았습니다.)"),
    INVALID_MOVE(HttpStatus.BAD_REQUEST, "SLOT-007", "같은 슬롯으로 건물 이동은 불가능합니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}