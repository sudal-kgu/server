package store.sonyk9919.api.domain.analysis.async;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.analysis.client.AnalysisClient;
import store.sonyk9919.api.domain.analysis.dto.response.AnalysisResponseDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisAsyncTask {
    private final AnalysisClient analysisClient;

    @Async
    public CompletableFuture<AnalysisResponseDto> runAnalysis(String requestId) {
        try {
            AnalysisResponseDto response = analysisClient.analyzeImage(requestId);
            log.info("[Analysis] runAnalysis successfully: {}", requestId);
            return CompletableFuture.completedFuture(response);

        } catch (Exception e) {
            log.error("[Analysis] runAnalysis failed: {}", requestId, e);
            return CompletableFuture.failedFuture(e);
        }
    }
}