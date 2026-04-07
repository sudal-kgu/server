package store.sonyk9919.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.member.entity.RefreshToken;
import store.sonyk9919.api.domain.member.entity.RefreshTokenStatus;
import store.sonyk9919.api.domain.member.exception.MemberStatus;
import store.sonyk9919.api.domain.member.repository.RefreshTokenRepository;
import store.sonyk9919.api.global.common.exception.CustomException;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken(Long memberId, String token, Long expiry) {
        RefreshToken refreshToken = RefreshToken.from(memberId, token, expiry, RefreshTokenStatus.ACTIVATE);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken getRefreshToken(Long memberId, String token) {
        return refreshTokenRepository.findBy(memberId, token)
                .orElseThrow(() -> new CustomException(MemberStatus.REFRESH_TOKEN_UNAUTHORIZED));
    }

    public void upsertRefreshToken(Long memberId, String token, Long expiry) {
        RefreshToken refreshToken = RefreshToken.from(memberId, token, expiry, RefreshTokenStatus.ACTIVATE);
        refreshTokenRepository.upsert(refreshToken);
    }
}
