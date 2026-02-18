package store.sonyk9919.api.domain.analysis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@RequiredArgsConstructor
public enum  AnalysisStatus implements BaseResponseStatus {
    ANALYSIS_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS-001", "해당 분석 요청(request_id)을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}