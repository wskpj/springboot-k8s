package com.example.springboot_app.global.exception.strategy.warn;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BadRequestExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof IllegalArgumentException || e instanceof PropertyReferenceException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        return ApiError.of(GlobalError.BAD_REQUEST, path, e.getMessage());
    }

    @Override
    public void log(Exception e, String path) {
        log.warn("[WARN] Bad Request Warning at {}: {}", path, e.getMessage());
    }
}
