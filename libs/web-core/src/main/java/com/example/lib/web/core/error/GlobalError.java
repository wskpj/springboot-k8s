package com.example.lib.web.core.error;

import org.springframework.http.HttpStatus;

import com.example.lib.common.core.exception.ErrorType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalError implements ErrorType {
    
    // 4xx Client Errors
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G400", "Bad Request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "G401", "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "G403", "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "G404", "Not Found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G405", "Method Not Allowed"),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "G406", "Not Acceptable"),
    CONFLICT(HttpStatus.CONFLICT, "G409", "Conflict"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "G415", "Unsupported Media Type"),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "G422", "Unprocessable Entity"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "G429", "Too Many Requests"),

    // 5xx Server Errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G500", "Internal Server Error"),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "G502", "Bad Gateway"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "G503", "Service Unavailable"),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "G504", "Gateway Timeout");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public int getStatus() {
        return httpStatus.value();
    }
}