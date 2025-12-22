package com.example.lib.common.core.context;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 사용자 컨텍스트 추상화
 */
public record UserContext(
    String userId,
    String name,
    Set<String> roles,
    Map<String, Object> attributes
) {
    public static UserContext guest() {
        return new UserContext("guest", "Guest", Collections.emptySet(), Collections.emptyMap());
    }

    public boolean isGuest() {
        return "guest".equals(userId);
    }
}
