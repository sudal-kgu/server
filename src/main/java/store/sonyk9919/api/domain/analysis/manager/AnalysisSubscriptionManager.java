package store.sonyk9919.api.domain.analysis.manager;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.sonyk9919.api.domain.analysis.dto.AnalysisCacheDto;
import store.sonyk9919.api.domain.analysis.repository.AnalysisTempCache;
import store.sonyk9919.api.domain.sse.type.SseEventType;
import store.sonyk9919.api.domain.sse.service.SseEmitterService;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisSubscriptionManager {
    private final AnalysisTempCache tempCache;
    private final SseEmitterService sseEmitterService;

    public SseEmitter subscribe(String requestId) {
        validateSubscription(requestId);
        SseEmitter emitter = sseEmitterService.createEmitter(requestId);

        tempCache.get(requestId)
                .ifPresent(cacheDto -> handleCachedResult(requestId, cacheDto));

        return emitter;
    }

    private void handleCachedResult(String requestId, AnalysisCacheDto cacheDto) {
        log.info("[Analysis] Immediate delivery (Cache Hit) : {}", requestId);

        if (cacheDto.isSuccess()) {
            sseEmitterService.sendAndComplete(requestId, SseEventType.ANALYSIS_RESULT, cacheDto.getSuccessData());
        } else {
            sseEmitterService.sendAndComplete(requestId, SseEventType.ERROR, cacheDto.getErrorStatus());
        }
        tempCache.remove(requestId);
    }

    private void validateSubscription(String requestId) {
        if (!sseEmitterService.exists(requestId)) return;

        log.warn("[Analysis] Duplicate subscription attempt : {}", requestId);
        throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR,
                "Already subscribed to requestId: " + requestId);
    }
}
