package store.sonyk9919.api.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthUserInfoDto {
    private final String id;

    public static OAuthUserInfoDto from(KakaoUserInfoDto userInfo) {
        return new OAuthUserInfoDto(String.valueOf(userInfo.getId()));
    }
}
