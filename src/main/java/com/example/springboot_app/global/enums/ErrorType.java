package com.example.springboot_app.global.enums;

import org.springframework.http.HttpStatus;

public interface ErrorType {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
