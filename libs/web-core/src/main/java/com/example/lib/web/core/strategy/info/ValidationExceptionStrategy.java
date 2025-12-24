package com.example.lib.web.core.strategy.info;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.lib.web.core.error.GlobalError;
import com.example.lib.web.core.response.ApiError;
import com.example.lib.web.core.strategy.ExceptionHandleStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * MethodArgumentNotValidException(Bean Validation 실패)을 처리하는 전략입니다.
 */
@Slf4j
public class ValidationExceptionStrategy implements ExceptionHandleStrategy<MethodArgumentNotValidException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof MethodArgumentNotValidException;
    }

    @Override
    public ApiError handle(MethodArgumentNotValidException e, String path) {
        log(e, path);
        
        List<ApiError.FieldError> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiError.FieldError(
                        error.getField(), 
                        error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ApiError.ofValidation(GlobalError.BAD_REQUEST, path, details);
    }

    @Override
    public void log(MethodArgumentNotValidException e, String path) {
        log.info("[INFO] Validation Failed at {}: {}", path, e.getMessage());
    }
}
