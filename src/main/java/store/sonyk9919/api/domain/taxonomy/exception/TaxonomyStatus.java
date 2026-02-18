package store.sonyk9919.api.domain.taxonomy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@AllArgsConstructor
public enum TaxonomyStatus implements BaseResponseStatus {
    TAXONOMY_NOT_FOUND(HttpStatus.NOT_FOUND, "TAXONOMY-001", "제공된 카테고리에 일치하는 분류를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
