package com.example.lib.web.starter.internal.strategy;

import com.example.lib.common.core.exception.BaseDomainException;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 비즈니스 로직 예외(Domain Exception)를 처리하는 전략입니다.
 */
@Slf4j
public class DomainExceptionStrategy implements ExceptionHandleStrategy<BaseDomainException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BaseDomainException;
    }

    @Override
    public ApiError handle(BaseDomainException e, String path) {
        log(e, path);
        return ApiError.of(e.getErrorType(), path, e.getMessage());
    }

    @Override
    public void log(BaseDomainException e, String path) {
        log.warn("[WebStarter] Domain Exception at {}: {} ({})", path, e.getMessage(), e.getErrorType().getCode());
    }
}
