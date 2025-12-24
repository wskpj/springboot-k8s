package com.example.lib.web.core.cookie;

import java.util.Optional;

/**
 * HTTP 쿠키 조작을 담당하는 인터페이스입니다.
 */
public interface CookieManager {

    /**
     * 쿠키를 생성하여 현재 응답에 추가합니다.
     */
    void addCookie(String name, String value, int maxAge);

    /**
     * 특정 이름의 쿠키를 현재 응답에서 삭제합니다.
     */
    void removeCookie(String name);

    /**
     * 현재 요청에서 특정 이름의 쿠키 값을 가져옵니다.
     */
    Optional<String> getCookie(String name);
}
