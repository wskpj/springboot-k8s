package com.example.springboot_app.global.exception.strategy.warn;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.types.BusinessException;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BusinessExceptionStrategy implements ExceptionHandleStrategy<BusinessException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BusinessException;
    }

    @Override
    public ApiError handle(BusinessException e, String path) {
        log(e, path);
        return ApiError.of(e.getErrorType(), path, e.getDetails());
    }

    @Override
    public void log(BusinessException e, String path) {
        log.warn("[WARN] Business Warning at {}: {}", path, e.getMessage());
    }
}
