package com.example.lib.web.core.strategy.error;

import org.springframework.stereotype.Component;

import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.common.core.exception.SystemBaseException;
import com.example.lib.web.core.response.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SystemExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof SystemBaseException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        SystemBaseException se = (SystemBaseException) e;
        return ApiError.of(se.getErrorType(), path, se.getDetails());
    }

    @Override
    public void log(Exception e, String path) {
        log.error("[ERROR] System Error at {}: {}", path, e.getMessage());
    }
}