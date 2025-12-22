package com.example.lib.jpa.starter.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextHolder;

/**
 * JPA Auditing(@CreatedBy, @LastModifiedBy)을 위해 현재 사용자의 ID를 제공하는 클래스입니다.
 * 이제 시큐리티 프레임워크가 아닌, 도메인 UserContextHolder를 직접 참조합니다.
 */
@Component
public class JpaAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        UserContext context = UserContextHolder.getContext();
        
        if (context.isGuest()) {
            return Optional.empty();
        }
        
        return Optional.of(context.userId());
    }
}
