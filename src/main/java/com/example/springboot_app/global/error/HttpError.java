package com.example.springboot_app.global.error;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class HttpError {
    private String message;
    private int status;
    private String code;
    private String timestamp;
    private String requestId;
    private String instance;
    private Object details;

    public static HttpError of(ErrorCode errorCode, String requestId, String instance, Object details) {
        return HttpError.builder()
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
