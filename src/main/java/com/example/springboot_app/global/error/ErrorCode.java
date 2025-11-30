package com.example.springboot_app.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid input value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "Method not allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "Internal server error"),

    // User
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U001", "Email already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "User not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U003", "Invalid password"),

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "Unauthorized"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "Invalid token"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A003", "Invalid email or password");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
