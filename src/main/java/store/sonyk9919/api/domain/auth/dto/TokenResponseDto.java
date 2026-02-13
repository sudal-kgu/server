package store.sonyk9919.api.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {

    private final String name;
    private final String token;
    private final Long expiry;

    public static TokenResponseDto from(String name, String token, Long expiry) {
        return new TokenResponseDto(name, token, expiry);
    }
}
