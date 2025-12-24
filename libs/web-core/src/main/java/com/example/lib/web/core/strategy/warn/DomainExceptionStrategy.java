package com.example.lib.web.core.strategy.warn;

import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.common.core.exception.BaseDomainException;
import com.example.lib.web.core.response.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DomainExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BaseDomainException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        BaseDomainException de = (BaseDomainException) e;
        return ApiError.of(de.getErrorType(), path, de.getDetails());
    }

    @Override
    public void log(Exception e, String path) {
        log.warn("[WARN] Domain Error at {}: {}", path, e.getMessage());
    }
}
