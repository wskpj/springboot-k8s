package com.example.springboot_app.infrastructure.persistence.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextResolver;

import lombok.RequiredArgsConstructor;

/**
 * JPA Auditing(@CreatedBy, @LastModifiedBy)을 위해 현재 사용자를 제공하는 구현체.
 * UserContextResolver를 사용하여 특정 보안 프레임워크와의 결합도를 낮춥니다.
 */
@Component
@RequiredArgsConstructor
public class JpaAuditorAware implements AuditorAware<String> {

    private final UserContextResolver userContextResolver;

    @Override
    public Optional<String> getCurrentAuditor() {
        return userContextResolver.getCurrentUserContext()
                .map(UserContext::userId);
    }
}
