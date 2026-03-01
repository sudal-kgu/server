package store.sonyk9919.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.member.entitiy.RefreshToken;
import store.sonyk9919.api.domain.member.entitiy.RefreshTokenStatus;
import store.sonyk9919.api.domain.member.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken(Long memberId, String token, Long expiry) {
        RefreshToken refreshToken = RefreshToken.from(memberId, token, expiry, RefreshTokenStatus.ACTIVATE);
        refreshTokenRepository.save(refreshToken);
    }
}
