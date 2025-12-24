package com.example.lib.common.core.context.user;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 현재 사용자 정보를 정의하는 인터페이스입니다.
 */
public interface CurrentUser {
    Long userId();
    String name();
    String token();
    Set<String> roles();
    Map<String, Object> attributes();
    boolean isGuest();

    /**
     * 표준 사용자 정보 구현체입니다. 
     * 코어에서 이를 제공함으로써 스타터의 중복 구현을 방지합니다.
     */
    record Default(
        Long userId,
        String name,
        String token,
        Set<String> roles,
        Map<String, Object> attributes,
        boolean isGuest
    ) implements CurrentUser {}

    /**
     * 익명(게스트) 사용자를 반환합니다.
     */
    static CurrentUser anonymous() {
        return new Default(0L, "guest", null, Collections.emptySet(), Collections.emptyMap(), true);
    }
}
