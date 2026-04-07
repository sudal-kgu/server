package store.sonyk9919.api.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import store.sonyk9919.api.domain.auth.dto.KakaoUserInfoDto;
import store.sonyk9919.api.domain.auth.dto.OAuthUserInfoDto;
import store.sonyk9919.api.domain.auth.entity.KakaoApiClient;
import store.sonyk9919.api.domain.auth.entity.KakaoAuthClient;
import store.sonyk9919.api.domain.auth.entity.OAuthProvider;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.auth.exception.AuthStatus;
import store.sonyk9919.api.domain.auth.utils.RequestHeaderUtils;
import store.sonyk9919.api.global.common.exception.CustomException;

import static store.sonyk9919.api.domain.auth.dto.KakaoTokenDto.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProvider implements OAuthProvider {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;

    @Value("${custom.kakao.clientId}") private String clientId;
    @Value("${custom.kakao.clientSecret}") private String clientSecret;
    @Value("${custom.kakao.redirectUri}") private String redirectUri;

    @Override
    public boolean isSupported(OAuthProviderType type) {
        return type.equals(OAuthProviderType.KAKAO);
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String code) {
        RequestBodyDto requestBody = RequestBodyDto.from(clientId, code, clientSecret, redirectUri);
        try {
            TokenResponseDto token = kakaoAuthClient.getToken(requestBody.toFormData());
            KakaoUserInfoDto userInfo = kakaoApiClient.getUserInfo(RequestHeaderUtils.getBearerToken(token.getAccessToken()));
            return OAuthUserInfoDto.from(userInfo);
        } catch (HttpClientErrorException e) {
            log.info("[Kakao]error message={}", e.getMessage());
            if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new CustomException(AuthStatus.BAD_REQUEST);
            }
            throw new CustomException(AuthStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
