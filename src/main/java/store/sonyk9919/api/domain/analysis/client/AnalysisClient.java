package store.sonyk9919.api.domain.analysis.client;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisClient {

    private final RestClient fastApiClient;

    public AnalysisResponseDto analyzeImage(String requestId) {
        Map<String, String> body = new HashMap<>();
        body.put("request_id", requestId);

        return fastApiClient.post()
                .uri("/extracts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new CustomException(ErrorStatus.IMAGE_NOT_FOUND, requestId + "에 해당되는 이미지를 찾을 수 없습니다.");
                })
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new CustomException(ErrorStatus.BAD_REQUEST, "잘못된 분석 요청입니다.");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req,res) -> {
                    throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "FastAPI 연결 중 서버 에러입니다.");
                })
                .body(AnalysisResponseDto.class);
    }
}