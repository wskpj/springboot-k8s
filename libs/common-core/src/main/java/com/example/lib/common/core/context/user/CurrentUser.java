package com.example.lib.common.core.context.user;

import java.util.Map;
import java.util.Set;

/**
 * 현재 로그인한 사용자의 정보를 제공하는 인터페이스입니다.
 * 요청 스코프 프록시를 통해 서비스 레이어에서 안전하게 사용됩니다.
 */
public interface CurrentUser {
    Long userId();
    String name();
    String token();
    Set<String> roles();
    Map<String, Object> attributes();
    boolean isGuest();
}
