package store.sonyk9919.api.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import store.sonyk9919.api.domain.auth.dto.AuthDto;
import store.sonyk9919.api.domain.auth.dto.TokenResponseDto;
import store.sonyk9919.api.domain.auth.service.AuthLoginFacade;
import store.sonyk9919.api.domain.member.service.MemberAccountService;
import store.sonyk9919.api.global.cookie.CookieProvider;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthMemberMembershipTestController {

    private final AuthLoginFacade authLoginFacade;
    private final CookieProvider cookieProvider;
    private final MemberAccountService memberAccountService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody AuthDto authDto) {
        memberAccountService.createMemberAccount(authDto.getAccount(), authDto.getPassword());
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody AuthDto authDto, HttpServletResponse response) {
        List<TokenResponseDto> tokens = authLoginFacade.login(
                authDto.getAccount(),
                authDto.getPassword()
        );

        tokens.forEach(token -> cookieProvider.addCookie(
                response,
                token.getName(),
                token.getToken(),
                token.getExpiry()
        ));
    }

    @GetMapping("/test/user")
    @ResponseStatus(HttpStatus.OK)
    public void user() {}

    @GetMapping("/test/admin")
    @ResponseStatus(HttpStatus.OK)
    public void admin() {}
}