package com.example.springboot_app.domain.user.exception;

import com.example.lib.common.core.exception.BaseDomainException;
import com.example.lib.web.core.error.GlobalError;

public class UserException {

    public static class NotFound extends BaseDomainException {
        public NotFound() {
            super(GlobalError.NOT_FOUND, "User not found.");
        }
    }
}
