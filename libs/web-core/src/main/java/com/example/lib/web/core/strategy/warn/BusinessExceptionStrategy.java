package com.example.lib.web.core.strategy.warn;

import org.springframework.stereotype.Component;

import com.example.lib.web.core.strategy.ExceptionHandleStrategy;
import com.example.lib.common.core.exception.BusinessBaseException;
import com.example.lib.web.core.response.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BusinessExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BusinessBaseException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        BusinessBaseException be = (BusinessBaseException) e;
        return ApiError.of(be.getErrorType(), path, be.getDetails());
    }

    @Override
    public void log(Exception e, String path) {
        log.warn("[WARN] Business Error at {}: {}", path, e.getMessage());
    }
}
