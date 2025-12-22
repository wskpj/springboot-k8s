package com.example.springboot_app.global.context;

import java.util.Optional;

/**
 * 현재 요청을 수행하는 사용자의 컨텍스트를 해결하는 인터페이스
 */
public interface UserContextResolver {
    
    /**
     * 현재 사용자의 컨텍스트 정보를 반환합니다.
     */
    Optional<UserContext> getCurrentUserContext();
}
