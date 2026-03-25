package store.sonyk9919.api.domain.analysis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import store.sonyk9919.api.global.common.dto.BaseResponseStatus;

@Getter
@AllArgsConstructor
public enum AnalysisStatus implements BaseResponseStatus {
    ANALYSIS_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS_REQUEST-001", "해당 분석 요청을 찾을 수 없습니다."),
    ANALYSIS_RESULT_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS_RESULT-001", "해당 결과를 찾을 수 없습니다."),

    ANALYSIS_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "ANALYSIS-002", "분석할 이미지를 찾을 수 없습니다."),
    ANALYSIS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "ANALYSIS-003", "추론 중 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public static AnalysisStatus fromFastApiError(String error) {
        return switch (error) {
            case "IMAGE_NOT_FOUND" -> ANALYSIS_IMAGE_NOT_FOUND;
            case "ANALYSIS_FAILED" -> ANALYSIS_FAILED;
            default -> ANALYSIS_FAILED;
        };
    }
}
