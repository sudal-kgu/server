package store.sonyk9919.api.global.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.global.jwt.dto.ClaimsConvertible;
import store.sonyk9919.api.global.jwt.exception.JwtStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secret}") String key) {
        secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
    }

    public String createJwt(Claims claims, long expirationMs) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .issuedAt(now)
                .expiration(expiration)
                .compact();
    }

    public Claims createClaims(ClaimsConvertible convertible) {
        return Jwts.claims()
                .subject(convertible.getSubject())
                .add(convertible.getBody())
                .build();
    }

    public Claims parseJwt(String jwt) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new CustomException(JwtStatus.JWT_EXPIRY_REQUEST);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(JwtStatus.JWT_BAD_REQUEST);
        }
    }
}
