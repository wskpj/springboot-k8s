package com.example.springboot_app.global.exception.strategy.error;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.types.SystemException;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SystemExceptionStrategy implements ExceptionHandleStrategy<SystemException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemException;
    }

    @Override
    public ApiError handle(SystemException e, String path) {
        log(e, path);
        return ApiError.of(e.getErrorType(), path, e.getDetails());
    }

    @Override
    public void log(SystemException e, String path) {
        log.error("[ERROR] System Error at {}: {}", path, e.getMessage(), e);
    }
}
