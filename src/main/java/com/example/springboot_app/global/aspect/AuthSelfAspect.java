package com.example.springboot_app.global.aspect;

import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import com.example.springboot_app.domain.auth.annotations.AuthSelf;
import com.example.springboot_app.infrastructure.security.dto.AuthUser;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthSelfAspect {
    @Before("@annotation(authSelf)")
    public void check(AuthSelf authSelf) {
        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
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
