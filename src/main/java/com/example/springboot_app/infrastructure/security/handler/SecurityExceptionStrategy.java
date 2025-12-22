package com.example.springboot_app.infrastructure.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 관련 예외(인증/인가)를 처리하는 전략
 */
@Slf4j
@Component
public class SecurityExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof AuthenticationException || e instanceof AccessDeniedException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        
        if (e instanceof AuthenticationException) {
            return ApiError.of(GlobalError.UNAUTHORIZED, path, e.getMessage());
        }
        
        return ApiError.of(GlobalError.FORBIDDEN, path, e.getMessage());
    }

    @Override
    public void log(Exception e, String path) {
        log.warn("[SECURITY] Security Exception at {}: {}", path, e.getMessage());
    }
}
