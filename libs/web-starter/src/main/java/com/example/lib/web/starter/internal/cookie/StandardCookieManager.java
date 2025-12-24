package com.example.lib.web.starter.internal.cookie;

import java.util.Optional;
import org.springframework.http.ResponseCookie;

import com.example.lib.web.core.cookie.CookieManager;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Servlet API를 사용하는 표준 쿠키 관리 구현체입니다.
 */
@RequiredArgsConstructor
public class StandardCookieManager implements CookieManager {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Override
    public void addCookie(String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    @Override
    public Optional<String> getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.ofNullable(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void removeCookie(String name) {
        addCookie(name, "", 0);
    }
}
