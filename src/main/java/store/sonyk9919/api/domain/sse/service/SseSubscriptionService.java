package store.sonyk9919.api.domain.sse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseSubscriptionService {
    private final SseEmitterService sseEmitterService;

    public SseEmitter createEmitter(String requestId) {
        validateSubscription(requestId);
        return sseEmitterService.createEmitter(requestId);
    }

    private void validateSubscription(String requestId) {
        if (!sseEmitterService.exists(requestId)) return;

        log.warn("[SSE] Duplicate subscription attempt. requestId={}", requestId);
        throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR,
                "Already subscribed to requestId: " + requestId);
    }
}
