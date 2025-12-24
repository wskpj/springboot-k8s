package com.example.lib.web.core.strategy.error;

import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.common.core.exception.BaseSystemException;
import com.example.lib.web.core.response.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BaseSystemException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        BaseSystemException se = (BaseSystemException) e;
        return ApiError.of(se.getErrorType(), path, se.getDetails());
    }

    @Override
    public void log(Exception e, String path) {
        log.error("[ERROR] System Error at {}: {}", path, e.getMessage(), e);
    }
}