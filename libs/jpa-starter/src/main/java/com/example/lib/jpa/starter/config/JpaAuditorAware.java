package com.example.lib.jpa.starter.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.example.lib.common.core.context.user.CurrentUser;
import com.example.lib.common.core.context.user.UserContextHolder;

/**
 * JPA Auditing(@CreatedBy, @UpdatedBy)을 위해 현재 사용자의 ID를 제공하는 클래스입니다.
 */
public class JpaAuditorAware implements AuditorAware<String> {
    
    @Override
    public Optional<String> getCurrentAuditor() {
        CurrentUser context = UserContextHolder.getContext();
        
        if (context.isGuest()) {
            return Optional.empty();
        }
        
        return Optional.of(String.valueOf(context.userId()));
    }
}
