package com.example.springboot_app.domain.user.exception;

import com.example.lib.common.core.exception.BusinessBaseException;
import com.example.lib.web.core.exception.GlobalError;

public class UserException {

    public static class NotFound extends BusinessBaseException {
        public NotFound() {
            super(GlobalError.NOT_FOUND, "User not found.");
        }
    }
}
