package store.sonyk9919.api.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.sonyk9919.api.domain.auth.dto.OAuthCodeDto;
import store.sonyk9919.api.domain.auth.entity.OAuthProviderType;
import store.sonyk9919.api.domain.auth.service.AuthLoginFacade;
import store.sonyk9919.api.global.cookie.CookieProvider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth")
public class OAuthController {

    private final AuthLoginFacade authLoginFacade;
    private final CookieProvider cookieProvider;

    @PostMapping("/kakao")
    @ResponseStatus(HttpStatus.OK)
    public void kakaoLogin(@RequestBody OAuthCodeDto oAuthCodeDto, HttpServletResponse response) {
        authLoginFacade.login(OAuthProviderType.KAKAO, oAuthCodeDto.getCode())
            .forEach(token -> cookieProvider.addCookie(response, token.getName(), token.getToken(), token.getExpiry()));
    }
}
