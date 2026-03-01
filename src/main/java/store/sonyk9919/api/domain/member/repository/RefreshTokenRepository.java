package store.sonyk9919.api.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;
import store.sonyk9919.api.domain.member.entitiy.RefreshToken;
import store.sonyk9919.api.domain.member.exception.MemberStatus;
import store.sonyk9919.api.global.common.exception.CustomException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String REFRESH_TOKEN_PREFIX = "RT";
    private final String INDEX_PREFIX = "INDEX";
    private final Long GRACE_PERIOD = 5L;

    private final RedisScript<Boolean> clearMemberRefreshTokens;
    private final ObjectMapper objectMapper;

    private String refreshTokenKey(Long memberId) {
        return REFRESH_TOKEN_PREFIX + ":" + memberId;
    }

    private String refreshTokenKey(Long memberId, String token) {
        return REFRESH_TOKEN_PREFIX + ":" + memberId + ":" + token;
    }

    private String refreshTokenIndexKey(Long memberId) {
        return REFRESH_TOKEN_PREFIX + ":" + INDEX_PREFIX + ":" + memberId;
    }

    public void save(RefreshToken refreshToken) {
        List<String> keys = List.of(
                refreshTokenKey(refreshToken.getMemberId()),
                refreshTokenIndexKey(refreshToken.getMemberId()),
                refreshTokenKey(refreshToken.getMemberId(), refreshToken.getToken())
        );

        try {
            redisTemplate.execute(
                    clearMemberRefreshTokens,
                    keys,
                    refreshToken.getToken(),
                    objectMapper.writeValueAsString(refreshToken),
                    String.valueOf(refreshToken.getExpiry())
            );
        } catch (Exception e) {
            throw new CustomException(MemberStatus.REFRESH_TOKEN_INTERNAL_SERVER_ERROR);
        }
    }
}
