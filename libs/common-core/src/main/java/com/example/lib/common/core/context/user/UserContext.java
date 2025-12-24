package com.example.lib.common.core.context.user;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 사용자 컨텍스트 데이터 객체 (Record)
 */
public record UserContext(
    Long userId,
    String name,
    Set<String> roles,
    Map<String, Object> attributes
) implements CurrentUser {
    
    public static UserContext guest() {
        return new UserContext(-1L, "Guest", Collections.emptySet(), Collections.emptyMap());
    }

    @Override
    public boolean isGuest() {
        return -1L == userId;
    }
}
