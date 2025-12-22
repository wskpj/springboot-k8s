package com.example.lib.security.starter.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.lib.common.core.context.UserContext;
import com.example.lib.common.core.context.UserContextHolder;
import com.example.lib.security.starter.dto.AuthUser;

/**
 * 컨트롤러 메서드 파라미터로 AuthUser DTO를 주입받을 수 있게 해주는 리졸버입니다.
 * 이제 시큐리티 프레임워크가 아닌, 도메인 UserContextHolder를 직접 참조합니다.
 */
@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        
        UserContext context = UserContextHolder.getContext();
        
        if (context.isGuest()) {
            return null;
        }
        
        // 도메인 컨텍스트의 정보를 바탕으로 프레임워크용 AuthUser DTO 생성
        return new AuthUser(Long.parseLong(context.userId()), context.name(), null);
    }
}
