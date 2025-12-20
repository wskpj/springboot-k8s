package com.example.springboot_app.global.exception.strategy.error;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.types.InfrastructureException;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InfrastructureExceptionStrategy implements ExceptionHandleStrategy<InfrastructureException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof InfrastructureException;
    }

    @Override
    public ApiError handle(InfrastructureException e, String path) {
        log(e, path);
        return ApiError.of(e.getErrorType(), path, e.getDetails());
    }

    @Override
    public void log(InfrastructureException e, String path) {
        log.error("[ERROR] Infrastructure Error at {}: {}", path, e.getMessage(), e);
    }
}
