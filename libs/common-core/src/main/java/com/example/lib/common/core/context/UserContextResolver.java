package com.example.lib.common.core.context;

import java.util.Optional;

/**
 * 현재 요청을 수행하는 사용자의 컨텍스트 리졸버
 */
public interface UserContextResolver {
    
    Optional<UserContext> getCurrentUserContext();
}
