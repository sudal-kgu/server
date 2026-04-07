package store.sonyk9919.api.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.sonyk9919.api.domain.auth.entity.OAuthProvider;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.auth.exception.AuthStatus;
import store.sonyk9919.api.global.common.exception.CustomException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuthProviderFactory {

    private final List<OAuthProvider> oAuthProviders;

    public OAuthProvider getProvider(OAuthProviderType type) {
        return oAuthProviders.stream()
                .filter(oAuthProvider -> oAuthProvider.isSupported(type))
                .findFirst()
                .orElseThrow(() -> new CustomException(AuthStatus.NOT_SUPPORTED_OAUTH_PROVIDER));
    }
}
