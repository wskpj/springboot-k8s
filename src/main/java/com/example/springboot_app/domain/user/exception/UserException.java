package com.example.springboot_app.domain.user.exception;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.types.BusinessException;

public class UserException {

    public static class NotFound extends BusinessException {
        public NotFound() {
            super(GlobalError.NOT_FOUND, "User not found.");
        }
    }
}
