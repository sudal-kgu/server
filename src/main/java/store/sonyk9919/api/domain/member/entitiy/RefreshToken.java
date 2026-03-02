package store.sonyk9919.api.domain.member.entitiy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

    private Long memberId;
    private String token;
    private Long expiry;
    private RefreshTokenStatus status;

    public static RefreshToken from(Long memberId, String token, Long expiry, RefreshTokenStatus status) {
        return new RefreshToken(memberId, token, expiry, status);
    }
}
