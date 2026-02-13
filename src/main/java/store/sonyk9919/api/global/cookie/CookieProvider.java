package store.sonyk9919.api.global.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import store.sonyk9919.api.global.config.property.CookieProperty;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private final CookieProperty cookieProperty;

    public void addCookie(HttpServletResponse response, String name, String value, Long expiry) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(expiry)
                .sameSite(cookieProperty.getSameSite())
                .domain(cookieProperty.getDomain())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void expireCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .maxAge(0)
                .domain(cookieProperty.getDomain())
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String resolveCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
