package com.example.springboot_app.global.exception.strategy;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.types.SystemException;
import com.example.springboot_app.global.response.types.ApiError;

@Component
public class SystemExceptionStrategy implements ExceptionHandleStrategy<SystemException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemException;
    }

    @Override
    public ApiError handle(SystemException e, String path) {
        return ApiError.of(e.getErrorType(), path, e.getDetails());
    }
}
