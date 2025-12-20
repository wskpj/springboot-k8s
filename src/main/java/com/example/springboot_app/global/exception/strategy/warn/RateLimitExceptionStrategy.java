package com.example.springboot_app.global.exception.strategy.warn;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.types.RateLimitException;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RateLimitExceptionStrategy implements ExceptionHandleStrategy<RateLimitException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof RateLimitException;
    }

    @Override
    public ApiError handle(RateLimitException e, String path) {
        log(e, path);
        return ApiError.of(e.getErrorType(), path, e.getDetails());
    }

    @Override
    public void log(RateLimitException e, String path) {
        log.warn("[WARN] Rate Limit Exceeded at {}: {}", path, e.getMessage());
    }
}
