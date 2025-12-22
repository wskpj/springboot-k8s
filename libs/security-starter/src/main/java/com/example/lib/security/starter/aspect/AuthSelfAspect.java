package com.example.lib.security.starter.aspect;

import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextHolder;
import com.example.lib.security.starter.annotation.AuthSelf;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @AuthSelf 어노테이션이 붙은 메서드 실행 전, 리소스의 소유권을 검증하는 Aspect입니다.
 */
@Aspect
@Component
public class AuthSelfAspect {

    @Before("@annotation(authSelf)")
    public void check(AuthSelf authSelf) {
        UserContext user = UserContextHolder.getContext();
        
        if (user.isGuest()) {
            throw new AccessDeniedException("Authentication is required");
        }
        
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        @SuppressWarnings("unchecked")
        var pathVars = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        
        String pathValue = pathVars != null ? pathVars.get(authSelf.value()) : null;
        if (pathValue == null) {
            throw new AccessDeniedException("Required path variable '" + authSelf.value() + "' is missing");
        }

        // 도메인 컨텍스트의 userId와 경로 변수를 직접 비교 (타입 독립성 확보)
        if (!user.userId().equals(pathValue)) {
            throw new AccessDeniedException("Access Denied: Resource ownership mismatch");
        }
    }
}
