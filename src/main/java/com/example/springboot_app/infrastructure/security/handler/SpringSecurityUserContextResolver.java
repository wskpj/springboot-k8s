package com.example.springboot_app.infrastructure.security.handler;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.context.UserContext;
import com.example.springboot_app.global.context.UserContextResolver;

/**
 * Spring Security의 SecurityContextHolder를 사용하여 사용자 정보를 해결하는 구현체
 */
@Component
public class SpringSecurityUserContextResolver implements UserContextResolver {

    @Override
    public Optional<UserContext> getCurrentUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            Set<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            return Optional.of(new UserContext(
                authentication.getName(), 
                authentication.getName(),
                roles,
                Collections.emptyMap()
            ));
        }
        
        return Optional.empty();
    }
}
