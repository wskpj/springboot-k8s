package com.example.springboot_app.global.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    // Common (C)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid input value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "Method not allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "Internal server error"),

    // User (U)
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U001", "Email already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "User not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U003", "Invalid password"),

    // Auth (A)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "Unauthorized"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "Invalid token"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A003", "Invalid email or password"),

    // Business (B)
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "B001", "Coupon not found"),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "B002", "Coupon stock is exhausted"),
    ALREADY_ISSUED(HttpStatus.CONFLICT, "B003", "Coupon already issued to this user"),

    // Infrastructure (I)
    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "I001", "Redis operation failed"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "I002", "Database operation failed"),

    // Validation (V)
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "V001", "Invalid parameter value");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorType(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
