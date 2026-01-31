package store.sonyk9919.api.domain.analysis.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.analysis.client.AnalysisClient;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisAsyncTask {
    private final AnalysisClient analysisClient;
    private final Map<String, Object> storage = new ConcurrentHashMap<>();

    @Async
    public void runAnalysis(String requestId) {
        log.info("Start Analysis: requestId={}", requestId);
        try {
            AnalysisResponseDto response = analysisClient.analyzeImage(requestId);
            storage.put(requestId, response);
            log.info("Analysis Completed: requestId={}", requestId);
        } catch (Exception e) {
            log.error("Analysis Error: requestId={}", requestId, e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }
}