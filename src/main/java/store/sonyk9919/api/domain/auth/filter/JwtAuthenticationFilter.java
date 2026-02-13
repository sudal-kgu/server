package store.sonyk9919.api.domain.auth.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import store.sonyk9919.api.domain.auth.dto.AuthMemberDto;
import store.sonyk9919.api.domain.auth.dto.TokenCookieName;
import store.sonyk9919.api.global.common.exception.CustomException;
import store.sonyk9919.api.global.cookie.CookieProvider;
import store.sonyk9919.api.global.jwt.service.JwtProvider;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CookieProvider cookieProvider;
    private final JwtProvider jwtProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = cookieProvider.resolveCookie(request, TokenCookieName.ACCESS_TOKEN);

        try {
            if (accessToken != null) {
                Claims claims = jwtProvider.parseJwt(accessToken);
                injectSecurityContext(AuthMemberDto.from(claims));
            }
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            cookieProvider.expireCookie(response, TokenCookieName.ACCESS_TOKEN);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

    }

    private void injectSecurityContext(AuthMemberDto member) {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().getKey()));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(member, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
