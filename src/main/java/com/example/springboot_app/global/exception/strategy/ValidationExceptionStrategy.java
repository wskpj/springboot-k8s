package com.example.springboot_app.global.exception.strategy;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.response.types.ApiError;

@Component
public class ValidationExceptionStrategy implements ExceptionHandleStrategy<MethodArgumentNotValidException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof MethodArgumentNotValidException;
    }

    @Override
    public ApiError handle(MethodArgumentNotValidException e, String path) {
        List<FieldErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return ApiError.of(GlobalError.BAD_REQUEST, path, details);
    }

    private record FieldErrorDetail(String field, String message) {}
}
