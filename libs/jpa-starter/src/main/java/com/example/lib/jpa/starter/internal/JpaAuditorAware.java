package com.example.lib.jpa.starter.internal;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.common.core.context.user.UserContextHolder;

/**
 * JPA Auditing(@CreatedBy, @UpdatedBy)을 위해 현재 사용자의 ID를 제공하는 클래스입니다.
 * BaseEntity의 createdBy/updatedBy 필드가 String 타입이므로 AuditorAware<String>을 구현합니다.
 */
public class JpaAuditorAware implements AuditorAware<String> {
    
    @Override
    public Optional<String> getCurrentAuditor() {
        UserContext context = UserContextHolder.getContext();
        
        if (context.user().isGuest()) {
            return Optional.empty();
        }
        
        return Optional.ofNullable(context.user().userId())
                .map(String::valueOf);
    }
}
