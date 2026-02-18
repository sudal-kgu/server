package store.sonyk9919.api.domain.analysis.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import store.sonyk9919.api.domain.analysis.dto.response.AnalysisResponseDto;
import store.sonyk9919.api.global.common.dto.ErrorStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("analysis-result")
public class AnalysisCache {
    @Id
    private String requestId;

    private AnalysisResponseDto successData;
    private ErrorStatus errorStatus;

    @TimeToLive
    private Long expiration;

    private AnalysisCache(String requestId, AnalysisResponseDto successData, Long expiration) {
        this.requestId = requestId;
        this.successData = successData;
        this.expiration = expiration;
    }

    private AnalysisCache(String requestId,  ErrorStatus status, Long expiration) {
        this.requestId = requestId;
        this.errorStatus = status;
        this.expiration = expiration;
    }

    public static AnalysisCache success(String requestId, AnalysisResponseDto data, Long ttl) {
        return new AnalysisCache(requestId, data, ttl);
    }

    public static AnalysisCache error(String requestId, ErrorStatus status, Long ttl) {
        return new AnalysisCache(requestId, status, ttl);
    }

    public boolean isSuccess() {
        return successData != null;
    }
}
