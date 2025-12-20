package com.example.springboot_app.global.exception.strategy;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.types.InfrastructureException;
import com.example.springboot_app.global.response.types.ApiError;

@Component
public class InfrastructureExceptionStrategy implements ExceptionHandleStrategy<InfrastructureException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof InfrastructureException;
    }

    @Override
    public ApiError handle(InfrastructureException e, String path) {
        return ApiError.of(e.getErrorType(), path, e.getDetails());
    }
}
