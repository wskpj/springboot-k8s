package com.example.springboot_app.global.exception.strategy;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.types.BusinessException;
import com.example.springboot_app.global.response.types.ApiError;

@Component
public class BusinessExceptionStrategy implements ExceptionHandleStrategy<BusinessException> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof BusinessException;
    }

    @Override
    public ApiError handle(BusinessException e, String path) {
        return ApiError.of(e.getErrorType(), path, e.getDetails());
    }
}
