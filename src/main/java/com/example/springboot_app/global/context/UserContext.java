package com.example.springboot_app.global.context;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 시스템 전반에서 사용되는 추상화된 사용자 컨텍스트
 * 로깅, 감사(Auditing), 권한 체크 등 다양한 용도로 확장 가능
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
