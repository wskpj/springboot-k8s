package com.example.lib.web.core.response;

import com.example.lib.common.core.exception.ErrorType;
import java.util.List;

/**
 * API 에러 응답을 담는 불변 객체(Record)입니다.
 */
public record ApiError(
    String message,
    int status,
    String code,
    String instance,
    Object details
) {
    public record FieldError(String field, String value, String reason) {}

    public static ApiError of(ErrorType errorType, String instance, Object details) {
        return new ApiError(
                errorType.getMessage(),
                errorType.getStatus(),
                errorType.getCode(),
                instance,
                details
        );
    }

    public static ApiError ofValidation(ErrorType errorType, String instance, List<FieldError> fieldErrors) {
        return new ApiError(
                errorType.getMessage(),
                errorType.getStatus(),
                errorType.getCode(),
                instance,
                fieldErrors
        );
    }
}