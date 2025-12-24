package com.example.lib.security.starter.internal.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;

import com.example.lib.common.core.context.user.UserContext;
import com.example.lib.common.core.context.user.UserContextHolder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @AuthAdmin 어노테이션이 붙은 메서드 호출 전 권한을 검사하는 Aspect입니다.
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class AuthAdminAspect {

    @Before("@annotation(com.example.lib.security.core.annotation.AuthAdmin) || @within(com.example.lib.security.core.annotation.AuthAdmin)")
    public void checkAdmin() {
        UserContext context = UserContextHolder.getContext();
        
        // context.user()는 항상 존재함을 신뢰할 수 있습니다.
        if (context.user().isGuest()) {
            throw new AccessDeniedException("로그인이 필요한 서비스입니다.");
        }
        
        if (!context.user().roles().contains("ROLE_ADMIN")) {
            log.warn("[AUTH] Admin access denied for user: {}", context.user().userId());
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }
    }
}
