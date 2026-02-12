package store.sonyk9919.api.domain.analysis.async;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.analysis.dto.AnalysisCacheDto;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.repository.AnalysisTempCache;
import store.sonyk9919.api.domain.sse.type.SseEventType;
import store.sonyk9919.api.domain.sse.service.SseEmitterService;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisCompletionHandler {
    private final AnalysisTempCache tempCache;
    private final SseEmitterService sseEmitterService;

    public void registerCallbacks(CompletableFuture<AnalysisResponseDto> future, String requestId) {
        future.thenAccept(result -> {
            handleSuccess(requestId, result);
        }).exceptionally(ex -> {
            handleFailure(requestId, ex);
            return null;
        });
    }

    private void handleSuccess(String requestId, AnalysisResponseDto result) {
        sseEmitterService.sendAndComplete(requestId, SseEventType.ANALYSIS_RESULT, result);
        saveToCache(requestId, AnalysisCacheDto.success(result));
    }

    private void handleFailure(String requestId, Throwable ex) {
        log.error("[Analysis] Handler sending error response : {}", requestId, ex);
        ErrorStatus errorStatus = ErrorStatus.INTERNAL_SERVER_ERROR;

        sseEmitterService.sendAndComplete(requestId, SseEventType.ERROR, errorStatus);
        saveToCache(requestId, AnalysisCacheDto.error(errorStatus));
    }

    private void saveToCache(String requestId, AnalysisCacheDto cacheDto) {
        try {
            tempCache.save(requestId, cacheDto);
        } catch (Exception e) {
            log.error("[Analysis] Failed to cache result: {}", requestId, e);
        }
    }
}