package com.example.springboot_app.global.exception.strategy;

import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.response.types.ApiError;

@Component
public class ExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return true;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        return ApiError.of(GlobalError.INTERNAL_SERVER_ERROR, path, e.getMessage());
    }
}
