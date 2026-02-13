package store.sonyk9919.api.domain.analysis.async;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.analysis.entity.AnalysisCache;
import store.sonyk9919.api.domain.analysis.repository.redis.AnalysisCacheRepository;
import store.sonyk9919.api.domain.sse.service.SseEmitterService;
import store.sonyk9919.api.domain.sse.type.SseEventType;
import store.sonyk9919.api.global.common.dto.BaseResponse;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisCompletionHandler {
    private final AnalysisCacheRepository analysisCacheRepository;
    private final SseEmitterService sseEmitterService;

    @Value("${custom.cache.analysis.ttl:300}")
    private long cacheTtl;

    public void registerCallbacks(CompletableFuture<AnalysisResponseDto> future, String requestId) {
        future.thenAccept(result -> {
            handleSuccess(requestId, result);
        }).exceptionally(ex -> {
            handleFailure(requestId, ex);
            return null;
        });
    }

    private void handleSuccess(String requestId, AnalysisResponseDto result) {
        sseEmitterService.sendAndComplete(
                requestId,
                SseEventType.ANALYSIS_RESULT,
                BaseResponse.success(result).getBody()
        );

        AnalysisCache cache = AnalysisCache.success(requestId, result, cacheTtl);
        analysisCacheRepository.save(cache);
    }

    private void handleFailure(String requestId, Throwable ex) {
        log.error("[Analysis] Handler sending error response : {}", requestId, ex);
        ErrorStatus errorStatus = ErrorStatus.INTERNAL_SERVER_ERROR;

        sseEmitterService.sendAndComplete(
                requestId,
                SseEventType.ERROR,
                BaseResponse.error(errorStatus).getBody()
        );

        AnalysisCache cache = AnalysisCache.error(requestId, errorStatus, cacheTtl);
        analysisCacheRepository.save(cache);
    }
}