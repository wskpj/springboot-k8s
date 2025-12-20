package com.example.springboot_app.global.exception.strategy.info;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.strategy.ExceptionHandleStrategy;
import com.example.springboot_app.global.response.types.ApiError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ValidationExceptionStrategy implements ExceptionHandleStrategy<MethodArgumentNotValidException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof MethodArgumentNotValidException;
    }

    @Override
    public ApiError handle(MethodArgumentNotValidException e, String path) {
        log(e, path);
        
        List<FieldErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return ApiError.of(GlobalError.BAD_REQUEST, path, details);
    }

    @Override
    public void log(MethodArgumentNotValidException e, String path) {
        log.info("[INFO] Validation Failed at {}: {}", path, e.getMessage());
    }

    private record FieldErrorDetail(String field, String message) {}
}
