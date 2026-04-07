package store.sonyk9919.api.domain.auth.entity;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import store.sonyk9919.api.domain.auth.dto.KakaoUserInfoDto;

public interface KakaoApiClient {
    @PostExchange(
            url = "/v2/user/me",
            contentType = "application/x-www-form-urlencoded;charset=utf-8"
    )
    KakaoUserInfoDto getUserInfo(@RequestHeader("Authorization") String accessToken);
}
