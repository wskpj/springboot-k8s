package com.example.springboot_app.domain.common.exception;

import com.example.springboot_app.global.exception.enums.GlobalError;
import com.example.springboot_app.global.exception.types.base.BusinessBaseException;

public class BusinessException {

    public static class RateLimit extends BusinessBaseException {
        public RateLimit() {
            super(GlobalError.TOO_MANY_REQUESTS);
        }

        public RateLimit(Object details) {
            super(GlobalError.TOO_MANY_REQUESTS, details);
        }
    }
}
