package store.sonyk9919.api.domain.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.dto.TokenCookieName;
import store.sonyk9919.api.domain.auth.dto.TokenResponseDto;
import store.sonyk9919.api.global.jwt.service.JwtProvider;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthTokenIssuer {

    private final JwtProvider jwtProvider;

    @Value("${jwt.expiry.access}")
    private long accessTokenExpiry;

    @Value("${jwt.expiry.refresh}")
    private long refreshTokenExpiry;

    public List<TokenResponseDto> issue(AuthMemberDto member) {
        return List.of(
                issueAccessToken(member),
                issueRefreshToken(member)
        );
    }

    private TokenResponseDto issueAccessToken(AuthMemberDto member) {
        Claims claims = jwtProvider.createClaims(member);
        String jwt = jwtProvider.createJwt(claims, accessTokenExpiry * 1000);
        return TokenResponseDto.from(TokenCookieName.ACCESS_TOKEN, jwt, accessTokenExpiry);
    }

    private TokenResponseDto issueRefreshToken(AuthMemberDto member) {
        Claims claims = jwtProvider.createClaims(member);
        String jwt = jwtProvider.createJwt(claims, refreshTokenExpiry * 1000);
        return TokenResponseDto.from(TokenCookieName.REFRESH_TOKEN, jwt, refreshTokenExpiry);
    }
}
