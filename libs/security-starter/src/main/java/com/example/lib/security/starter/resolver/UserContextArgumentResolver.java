package com.example.lib.security.starter.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextHolder;

/**
 * 컨트롤러 메서드 파라미터로 도메인 UserContext를 주입받을 수 있게 해주는 리졸버입니다.
 */
@Component
public class UserContextArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        
        UserContext context = UserContextHolder.getContext();
        
        if (context.isGuest()) {
            return null;
        }
        
        return context;
    }
}
