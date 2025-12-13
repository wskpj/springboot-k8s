package com.example.springboot_app.api.common.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

import com.example.springboot_app.global.error.ErrorType;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private String code;
    private String timestamp;
    private String requestId;
    private String instance;
    private Object details;

    public static ErrorResponse of(ErrorType errorCode, String requestId, String instance, Object details) {
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .code(errorCode.getCode())
                .timestamp(LocalDateTime.now().toString())
                .requestId(requestId)
                .instance(instance)
                .details(details)
                .build();
    }
}
