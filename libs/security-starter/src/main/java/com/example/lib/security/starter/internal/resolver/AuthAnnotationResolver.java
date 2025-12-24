package com.example.lib.security.starter.internal.resolver;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.handler.MatchableHandlerMapping;

import com.example.lib.security.core.resolver.AuthResolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 요청 핸들러에 설정된 권한 관련 어노테이션을 분석하는 클래스입니다.
 */
@RequiredArgsConstructor
public class AuthAnnotationResolver implements AuthResolver {
    
    private final HandlerMappingIntrospector introspector;

    @Override
    public boolean hasAnnotation(HttpServletRequest request, Class<? extends Annotation> annotationClass) {
        try {
            MatchableHandlerMapping mapping = introspector.getMatchableHandlerMapping(request);
            if (mapping == null) return false;
            
            Object handler = mapping.getHandler(request).getHandler();
            if (handler instanceof HandlerMethod hm) {
                return AnnotatedElementUtils.hasAnnotation(hm.getMethod(), annotationClass) || // 메서드 어노테이션 먼저 체크
                       AnnotatedElementUtils.hasAnnotation(hm.getBeanType(), annotationClass); // 클래스(빈) 어노테이션 체크
            }
        } catch (Exception e) { 
            return false; 
        }
        return false;
    }
}
