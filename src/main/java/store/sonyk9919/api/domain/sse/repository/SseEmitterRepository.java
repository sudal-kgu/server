package store.sonyk9919.api.domain.sse.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String requestId, SseEmitter emitter) {
        emitters.put(requestId, emitter);
    }

    public Optional<SseEmitter> findByRequestId(String requestId) {
        SseEmitter sseEmitter = emitters.get(requestId);
        return Optional.ofNullable(sseEmitter);
    }

    public void deleteByRequestId(String requestId) {
        emitters.remove(requestId);
    }
}