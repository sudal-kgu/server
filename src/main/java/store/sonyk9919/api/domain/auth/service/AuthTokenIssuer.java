package store.sonyk9919.api.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.dto.TokenCookieName;
import store.sonyk9919.api.domain.auth.dto.TokenResponseDto;
import store.sonyk9919.api.domain.member.entity.RefreshToken;
import store.sonyk9919.api.domain.member.service.RefreshTokenService;
import store.sonyk9919.api.global.jwt.service.JwtProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthTokenIssuer {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.expiry.access}")
    private long accessTokenExpiry;

    @Value("${jwt.expiry.refresh}")
    private long refreshTokenExpiry;

    public List<TokenResponseDto> issue(AuthMemberDto member) {
        TokenResponseDto accessToken = createToken(member, TokenCookieName.ACCESS_TOKEN, accessTokenExpiry);
        TokenResponseDto refreshToken = createToken(member, TokenCookieName.REFRESH_TOKEN, refreshTokenExpiry);
        refreshTokenService.createRefreshToken(member.getId(), refreshToken.getToken(), refreshTokenExpiry);
        return List.of(accessToken, refreshToken);
    }

    public List<TokenResponseDto> reissue(AuthMemberDto member, String refreshToken) {
        RefreshToken oldRefreshToken = refreshTokenService.getRefreshToken(member.getId(), refreshToken);
        return switch (oldRefreshToken.getStatus()) {
            case STALE -> List.of(createToken(member, TokenCookieName.ACCESS_TOKEN, accessTokenExpiry));
            case ACTIVATE -> {
                TokenResponseDto newRefreshToken = createToken(member, TokenCookieName.REFRESH_TOKEN, refreshTokenExpiry);
                refreshTokenService.upsertRefreshToken(member.getId(), newRefreshToken.getToken(), refreshTokenExpiry);
                yield List.of(
                        createToken(member, TokenCookieName.ACCESS_TOKEN, accessTokenExpiry),
                        newRefreshToken
                );
            }
        };
    }

    private TokenResponseDto createToken(AuthMemberDto member, String name, Long expiry) {
        Claims claims = jwtProvider.createClaims(member);
        String jwt = jwtProvider.createJwt(claims, expiry * 1000);
        return TokenResponseDto.from(name, jwt, expiry);
    }
}
