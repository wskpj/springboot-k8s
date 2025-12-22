package com.example.lib.security.starter.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.lib.security.starter.dto.AuthUser;
import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextResolver;

import lombok.RequiredArgsConstructor;

/**
 * 컨트롤러 메서드 파라미터로 AuthUser DTO를 주입받을 수 있게 해주는 리졸버입니다.
 */
@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserContextResolver userContextResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return userContextResolver.getCurrentUserContext()
                .map(ctx -> new AuthUser(Long.parseLong(ctx.userId()), null, null)) // userId를 기반으로 AuthUser 생성
                .orElse(null);
    }
}
