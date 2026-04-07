package store.sonyk9919.api.domain.auth.entity;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import static store.sonyk9919.api.domain.auth.dto.KakaoTokenDto.*;

public interface KakaoAuthClient {
    @PostExchange(
            url = "/oauth/token",
            contentType = "application/x-www-form-urlencoded;charset=utf-8"
    )
    TokenResponseDto getToken(@RequestBody MultiValueMap<String, String> requestBodyDto);
}
