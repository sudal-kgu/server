package store.sonyk9919.api.domain.analysis.client;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;

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
                .body(AnalysisResponseDto.class);
    }
}