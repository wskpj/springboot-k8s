package com.example.springboot_app.global.exception.strategy;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.response.types.ApiError;

@Component
public class SecurityExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof AuthenticationException || e instanceof AccessDeniedException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        if (e instanceof AuthenticationException) {
            return ApiError.of(GlobalError.UNAUTHORIZED, path, e.getMessage());
        }
        return ApiError.of(GlobalError.FORBIDDEN, path, e.getMessage());
    }
}
