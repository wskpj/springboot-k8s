package com.example.springboot_app.global.exception.strategy.warn;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.exception.types.base.BaseException;
import com.example.springboot_app.global.exception.types.base.BusinessBaseException;
import com.example.springboot_app.global.response.types.ApiError;

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
        
        if (e instanceof BaseException be) return ApiError.of(be.getErrorType(), path, be.getDetails());
        
        return ApiError.of(GlobalError.BAD_REQUEST, path, e.getMessage());
    }

    @Override
    public void log(Exception e, String path) {
        log.warn("[WARN] Business Warning at {}: {}", path, e.getMessage());
    }
}
