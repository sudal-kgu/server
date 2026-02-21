package store.sonyk9919.api.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.sonyk9919.api.domain.sse.service.SseSubscriptionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisSseFacade {
    private final SseSubscriptionService sseSubscriptionService;
    private final AnalysisResultNotifier analysisResultNotifier;

    public SseEmitter subscribe(String requestId) {
        SseEmitter emitter = sseSubscriptionService.createEmitter(requestId);
        analysisResultNotifier.notifyIfAlreadyCompleted(requestId);
        return emitter;
    }
}
