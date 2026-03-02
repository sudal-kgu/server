package store.sonyk9919.api.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;
import store.sonyk9919.api.domain.member.entitiy.RefreshToken;
import store.sonyk9919.api.domain.member.entitiy.RefreshTokenStatus;
import store.sonyk9919.api.domain.member.exception.MemberStatus;
import store.sonyk9919.api.global.common.exception.CustomException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String REFRESH_TOKEN_PREFIX = "RT";
    private final String INDEX_PREFIX = "INDEX";
    private final Long GRACE_PERIOD = 5L;

    private final RedisScript<Boolean> clearMemberRefreshTokens;
    private final RedisScript<Boolean> rotateRefreshToken;
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

    public Optional<RefreshToken> findBy(Long memberId, String token) {
        String jsonString = (String) redisTemplate.opsForValue().get(refreshTokenKey(memberId, token));

        if (jsonString == null) return Optional.empty();
        try {
            RefreshToken refreshToken = objectMapper.readValue(jsonString, RefreshToken.class);
            return Optional.of(refreshToken);
        } catch (JacksonException e) {
            throw new CustomException(MemberStatus.REFRESH_TOKEN_INTERNAL_SERVER_ERROR);
        }
    }

    public void upsert(RefreshToken refreshToken) {
        List<String> keys = List.of(
                refreshTokenKey(refreshToken.getMemberId()),
                refreshTokenIndexKey(refreshToken.getMemberId()),
                refreshTokenKey(refreshToken.getMemberId(), refreshToken.getToken())
        );

        try {
            redisTemplate.execute(
                    rotateRefreshToken,
                    keys,
                    refreshToken.getToken(),
                    objectMapper.writeValueAsString(refreshToken),
                    String.valueOf(refreshToken.getExpiry()),
                    RefreshTokenStatus.STALE.toString(),
                    String.valueOf(GRACE_PERIOD)
            );
        } catch (Exception e) {
            throw new CustomException(MemberStatus.REFRESH_TOKEN_INTERNAL_SERVER_ERROR);
        }
    }
}
