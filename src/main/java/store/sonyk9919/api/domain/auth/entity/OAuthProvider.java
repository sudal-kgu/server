package store.sonyk9919.api.domain.auth.entity;

import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;

public interface OAuthProvider {
    boolean isSupported(OAuthProviderType type);
    OAuthUserInfoDto getUserInfo(String code);
}
