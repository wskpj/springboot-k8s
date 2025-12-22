package com.example.lib.common.core.exception;

public interface ErrorType {
    
    int getStatus();
    String getCode();
    String getMessage();
}