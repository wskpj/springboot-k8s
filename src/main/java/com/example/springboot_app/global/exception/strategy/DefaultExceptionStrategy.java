package com.example.springboot_app.global.exception.strategy;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return true;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);

        return ApiError.of(GlobalError.INTERNAL_SERVER_ERROR, path, e.getMessage());
    }

    @Override
    public void log(Exception e, String path) {
        log.error("[FATAL] Unhandled Error at {}: {}", path, e.getMessage(), e);
    }
}
