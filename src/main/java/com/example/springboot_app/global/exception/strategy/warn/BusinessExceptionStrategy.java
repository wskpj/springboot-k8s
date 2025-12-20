package com.example.springboot_app.global.exception.strategy.warn;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.types.BaseException;
import com.example.springboot_app.global.exception.types.BusinessException;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BusinessExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof AuthenticationException ||
               e instanceof AccessDeniedException ||
               e instanceof BusinessException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        
        if (e instanceof AuthenticationException) return ApiError.of(GlobalError.UNAUTHORIZED, path, e.getMessage());
        if (e instanceof AccessDeniedException) return ApiError.of(GlobalError.FORBIDDEN, path, e.getMessage());
        if (e instanceof BaseException be) return ApiError.of(be.getErrorType(), path, be.getDetails());
        
        return ApiError.of(GlobalError.BAD_REQUEST, path, e.getMessage());
    }

    @Override
    public void log(Exception e, String path) {
        log.warn("[WARN] Business Warning at {}: {}", path, e.getMessage());
    }
}
