package com.example.springboot_app.global.response.types;

import com.example.springboot_app.global.exception.enums.ErrorType;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApiError {
    private String message;
    private int status;
    private String code;
    private String instance;
    private Object details;

    public record FieldError(String field, String value, String reason) {}

    public static ApiError of(ErrorType errorType, String instance, Object details) {
        return ApiError.builder()
                .message(errorType.getMessage())
                .status(errorType.getStatus().value())
                .code(errorType.getCode())
                .instance(instance)
                .details(details)
                .build();
    }

    public static ApiError validation(ErrorType errorType, String instance, List<FieldError> fieldErrors) {
        return ApiError.builder()
                .message(errorType.getMessage())
                .status(errorType.getStatus().value())
                .code(errorType.getCode())
                .instance(instance)
                .details(fieldErrors)
                .build();
    }
}
