
package com.example.springboot_app.global.exception.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessError implements ErrorType {

    // User (U)
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "U001", "Email already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "User not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U003", "Invalid password"),

    // Auth (A)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A401", "Unauthorized"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A401", "Invalid token"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A401", "Invalid email or password"),
    TOO_MANY_LOGIN_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "A429", "Too many login attempts. Account temporarily locked."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A403", "Access denied"),

    // Coupon (C)
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "Coupon not found"),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "C002", "Coupon stock is exhausted"),
    ALREADY_ISSUED(HttpStatus.CONFLICT, "C003", "Coupon already issued to this user");

    private final HttpStatus status;
    private final String code;
    private final String message;

}