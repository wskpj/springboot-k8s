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

import com.example.lib.security.starter.annotation.AuthSelf;
import com.example.lib.security.starter.dto.AuthUser;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @AuthSelf 어노테이션이 붙은 메서드 실행 전, 리소스의 소유권을 검증하는 Aspect입니다.
 * 현재 인증된 사용자의 ID와 경로 변수(PathVariable)로 전달된 ID를 비교합니다.
 */
@Aspect
@Component
public class AuthSelfAspect {

    @Before("@annotation(authSelf)")
    public void check(AuthSelf authSelf) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof AuthUser)) {
            throw new AccessDeniedException("Authentication is required");
        }
        
        AuthUser user = (AuthUser) principal;
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        @SuppressWarnings("unchecked")
        var pathVars = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        
        String pathValue = pathVars != null ? pathVars.get(authSelf.value()) : null;
        if (pathValue == null) {
            throw new AccessDeniedException("Required path variable '" + authSelf.value() + "' is missing");
        }

        try {
            Long targetId = Long.parseLong(pathValue);
            if (!user.getId().equals(targetId)) {
                throw new AccessDeniedException("Access Denied: Resource ownership mismatch");
            }
        } catch (NumberFormatException e) {
            throw new AccessDeniedException("Invalid resource ID format");
        }
    }
}
