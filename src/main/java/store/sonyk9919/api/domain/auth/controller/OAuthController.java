package store.sonyk9919.api.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 인가 코드를 이용해 로그인을 진행하고, 액세스/리프레시 토큰을 쿠키에 설정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공 (토큰이 HttpServletResponse 쿠키에 포함됨)",
                    headers = @Header(name = "Set-Cookie", description = "access_token, refresh_token 등")
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 인가 코드"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/kakao")
    @ResponseStatus(HttpStatus.OK)
    public void kakaoLogin(
            @Parameter(description = "카카오에서 발급받은 인가 코드 객체", required = true)
            @RequestBody OAuthCodeDto oAuthCodeDto,
            @Parameter(hidden = true) HttpServletResponse response
    ) {
        authLoginFacade.login(OAuthProviderType.KAKAO, oAuthCodeDto.getCode())
                .forEach(token -> cookieProvider.addCookie(response, token.getName(), token.getToken(), token.getExpiry()));
    }
}
