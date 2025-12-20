package com.example.springboot_app.global.exception.strategy;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Component;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.response.types.ApiError;

@Component
public class BadRequestExceptionStrategy implements ExceptionHandleStrategy<Exception> {

    @Override
    public boolean supports(Exception e) {
        return e instanceof IllegalArgumentException || e instanceof PropertyReferenceException;
    }

    @Override
    public ApiError handle(Exception e, String path) {
        return ApiError.of(GlobalError.BAD_REQUEST, path, e.getMessage());
    }
}
