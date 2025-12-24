package com.example.lib.web.starter.internal.strategy;

import java.util.stream.Collectors;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.lib.web.core.error.GlobalError;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 데이터 유효성 검증 예외(Validation Exception)를 처리하는 전략입니다.
 */
@Slf4j
public class ValidationExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof MethodArgumentNotValidException || 
               e instanceof BindException || 
               e instanceof ConstraintViolationException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        log(e, path);
        
        String detail = extractDetail(e);
        return ApiError.of(GlobalError.BAD_REQUEST, path, detail);
    }

    private String extractDetail(Exception e) {
        if (e instanceof MethodArgumentNotValidException ex) {
            return ex.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        if (e instanceof BindException ex) {
            return ex.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        }
        if (e instanceof ConstraintViolationException ex) {
            return ex.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
        }
        return e.getMessage();
    }

    @Override
    public void log(Exception e, String path) {
        log.warn("[WebStarter] Validation Exception at {}: {}", path, e.getMessage());
    }
}
