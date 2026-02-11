package store.sonyk9919.api.domain.sse.service;

import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import store.sonyk9919.api.domain.analysis.dto.AnalysisResponseDto;
import store.sonyk9919.api.domain.sse.repository.SseEmitterRepository;
import store.sonyk9919.api.global.common.dto.BaseResponse;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterService {
    private final SseEmitterRepository sseEmitterRepository;

    @Value("${custom.sse.timeout}")
    private Long timeout;

    public SseEmitter createEmitter(String requestId) {
        SseEmitter emitter = new SseEmitter(timeout);
        sseEmitterRepository.save(requestId, emitter);

        setupEmitterCallbacks(emitter, requestId);
        sendEvent(requestId, "connect", BaseResponse.success("connected"));

        return emitter;
    }

    public boolean exists(String requestId) {
        return sseEmitterRepository.findByRequestId(requestId).isPresent();
    }

    public void sendResult(String requestId, AnalysisResponseDto data) {
        sendEvent(requestId, "analysis-result", BaseResponse.success(data));
        completeEmitter(requestId);
    }

    public void sendError(String requestId, ErrorStatus errorStatus) {
        sendEvent(requestId, "error", BaseResponse.error(errorStatus));
        completeEmitter(requestId);
    }

    private void sendEvent(String requestId, String eventName, Object data) {
        sseEmitterRepository.findByRequestId(requestId).ifPresent(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(requestId)
                        .name(eventName)
                        .data(data));
                log.debug("[SSE] Sent event '{}' : {}", eventName, requestId);
            } catch (IOException e) {
                log.warn("[SSE] Sent event failed: {}", requestId);
                sseEmitterRepository.deleteByRequestId(requestId);
            }
        });
    }

    private void completeEmitter(String requestId) {
        Optional<SseEmitter> sseEmitter = sseEmitterRepository.findByRequestId(requestId);
        sseEmitter.ifPresent(SseEmitter::complete);
    }

    private void setupEmitterCallbacks(SseEmitter emitter, String requestId) {
        emitter.onCompletion(() -> {
            log.debug("[SSE] Connection completed: {}", requestId);
            sseEmitterRepository.deleteByRequestId(requestId);
        });
        emitter.onTimeout(() -> {
            log.warn("[SSE] Connection Timeout: {}", requestId);
            sseEmitterRepository.deleteByRequestId(requestId);
        });
        emitter.onError((e) -> {
            log.error("[SSE] Connection error: {}", requestId, e);
            sseEmitterRepository.deleteByRequestId(requestId);
        });
    }
}