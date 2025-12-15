package com.example.springboot_app.global.response.types;

import com.example.springboot_app.global.exception.enums.ErrorType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiError {
    private String message;
    private int status;
    private String code;
    private String instance;
    private Object details;

    public static ApiError of(ErrorType errorType, String instance, Object details) {
        return ApiError.builder()
                .message(errorType.getMessage())
                .status(errorType.getStatus().value())
                .code(errorType.getCode())
                .instance(instance)
                .details(details)
                .build();
    }
}
