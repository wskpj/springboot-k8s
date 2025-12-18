package com.example.springboot_app.global.security.resolver;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.handler.MatchableHandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthAnnotationResolver {
    private final HandlerMappingIntrospector introspector;

    public boolean hasAnnotation(HttpServletRequest request, Class<? extends Annotation> annotationClass) {
        try {
            MatchableHandlerMapping mapping = introspector.getMatchableHandlerMapping(request);
            if (mapping == null) return false;
            Object handler = mapping.getHandler(request).getHandler();
            if (handler instanceof HandlerMethod hm) {
                return AnnotatedElementUtils.hasAnnotation(hm.getMethod(), annotationClass) || // 메서드 어노테이션 먼저 체크
                       AnnotatedElementUtils.hasAnnotation(hm.getBeanType(), annotationClass); // 클래스(빈) 어노테이션 체크
            }
        } catch (Exception e) { return false; }
        return false;
    }
}