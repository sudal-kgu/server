package store.sonyk9919.api.domain.analysis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@AllArgsConstructor
public enum AnalysisStatus implements BaseResponseStatus {
    ANALYSIS_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS_REQUEST-001", "해당 분석 요청을 찾을 수 없습니다."),
    ANALYSIS_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS_RESULT-001", "해당 결과를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
