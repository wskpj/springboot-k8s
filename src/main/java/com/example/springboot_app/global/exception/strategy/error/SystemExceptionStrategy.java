package com.example.springboot_app.global.exception.strategy.error;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.types.base.SystemBaseException;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SystemExceptionStrategy implements ExceptionHandleStrategy<SystemBaseException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemBaseException;
    }

    @Override
    public ApiError handle(SystemBaseException e, String path) {
        log(e, path);
        return ApiError.of(e.getErrorType(), path, e.getDetails());
    }

    @Override
    public void log(SystemBaseException e, String path) {
        log.error("[ERROR] System Error at {}: {}", path, e.getMessage());
    }
}
