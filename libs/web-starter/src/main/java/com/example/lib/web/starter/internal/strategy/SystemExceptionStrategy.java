package com.example.lib.web.starter.internal.strategy;

import com.example.lib.common.core.exception.BaseSystemException;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 시스템 레벨의 예외(System Exception)를 처리하는 전략입니다.
 */
@Slf4j
public class SystemExceptionStrategy implements ExceptionHandleStrategy<BaseSystemException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BaseSystemException;
    }

    @Override
    public ApiError handle(BaseSystemException e, String path) {
        log(e, path);
        return ApiError.of(e.getErrorType(), path, e.getMessage());
    }

    @Override
    public void log(BaseSystemException e, String path) {
        log.error("[WebStarter] System Exception at {}: {} ({})", path, e.getMessage(), e.getErrorType().getCode(), e);
    }
}