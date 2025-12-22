package com.example.lib.jpa.starter.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextResolver;

import lombok.RequiredArgsConstructor;

/**
 * JPA Auditing(@CreatedBy, @LastModifiedBy)을 위해 현재 사용자의 ID를 제공하는 클래스입니다.
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
