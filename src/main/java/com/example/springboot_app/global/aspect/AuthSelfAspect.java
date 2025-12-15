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
import com.example.springboot_app.global.security.AuthUser;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthSelfAspect {
    @Before("@annotation(authSelf)")
    public void check(AuthSelf authSelf) {
        AuthUser user = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var pathVars = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        
        Long targetId = Long.parseLong(pathVars.get(authSelf.value()));
        if (!user.getId().equals(targetId)) throw new AccessDeniedException("Access Denied");
    }
}
