package com.example.springboot_app.global.security.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.springboot_app.global.security.AuthUser;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter p) {
        return p.getParameterType().equals(AuthUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter p, ModelAndViewContainer m, NativeWebRequest w, WebDataBinderFactory f) {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
