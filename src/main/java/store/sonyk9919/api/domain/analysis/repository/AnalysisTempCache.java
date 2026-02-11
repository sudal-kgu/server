package store.sonyk9919.api.domain.analysis.repository;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.domain.analysis.dto.AnalysisCacheDto;
import store.sonyk9919.api.global.common.dto.ErrorStatus;
import store.sonyk9919.api.global.common.exception.CustomException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalysisTempCache {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${custom.cache.analysis.ttl:5m}")
    private Duration ttl;
    private static final String KEY_PREFIX = "analysis-result:";

    public void save(String requestId, AnalysisCacheDto result) {
        String key = getKey(requestId);

        try {
            String json = objectMapper.writeValueAsString(result);
            redisTemplate.opsForValue().set(key, json, ttl);
            log.debug("[Redis] Saved: {}", requestId);

        } catch (Exception e) {
            log.error("[Redis] Save failed: {}", requestId, e);
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "Failed to cache analysis result");
        }
    }

    public Optional<AnalysisCacheDto> get(String requestId) {
        String key = getKey(requestId);
        Object json = redisTemplate.opsForValue().get(key);

        if (json == null) {
            return Optional.empty();
        }

        try {
            AnalysisCacheDto cacheDto = objectMapper.readValue(json.toString(), AnalysisCacheDto.class);
            return Optional.of(cacheDto);
        } catch (Exception e) {
            log.error("[Redis] Parse failed : {}", requestId, e);
            return Optional.empty();
        }
    }

    public void remove(String requestId) {
        redisTemplate.delete(getKey(requestId));
        log.debug("[Redis] Removed : {}", requestId);
    }

    private String getKey(String requestId){
        return KEY_PREFIX + requestId;
    }
}
