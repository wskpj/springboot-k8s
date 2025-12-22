package com.example.springboot_app.infrastructure.security.handler;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.context.UserContextResolver;

/**
 * Spring Security의 SecurityContextHolder를 사용하여 사용자 정보를 해결하는 구현체
 */
@Component
public class SpringSecurityUserContextResolver implements UserContextResolver {

    @Override
    public Optional<String> getCurrentUserIdentifier() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return Optional.of(authentication.getName());
        }
        
        return Optional.empty();
    }
}
