package store.sonyk9919.api.domain.building.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum BuildingStatus implements BaseResponseStatus {
    NOT_PRODUCTION_BUILDING(HttpStatus.BAD_REQUEST, "BUILDING-001", "생산 시설이 아니므로 가동할 수 없습니다."),
    NOT_OPERATING(HttpStatus.BAD_REQUEST, "BUILDING-002", "가동 중인 건물이 아닙니다."),
    INVALID_COLLECT_TIME(HttpStatus.BAD_REQUEST, "BUILDING-003", "이전 수확 시간보다 과거일 수 없습니다."),

    BUILDING_YIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "BUILDING-004", "해당 레벨의 건물 스펙을 찾을 수 없습니다."),
    BUILDING_METADATA_NOT_FOUND(HttpStatus.NOT_FOUND, "BUILDING-005", "해당 건물 메타 테이터를 찾을 수 없습니다."),
    BUILDING_NOT_FOUND(HttpStatus.NOT_FOUND, "BUILDING-006", "건물을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}