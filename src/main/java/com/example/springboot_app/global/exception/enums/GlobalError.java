package com.example.springboot_app.global.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalError implements ErrorType {

    // 4xx
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G400", "Bad Request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "G401", "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "G403", "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "G404", "Not Found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G405", "Method not allowed"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "G429", "Too many requests"),

    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "Internal server error"),
    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G501", "Redis operation failed"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G502", "Database operation failed");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
