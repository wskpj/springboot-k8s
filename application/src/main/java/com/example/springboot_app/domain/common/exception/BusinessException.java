package com.example.springboot_app.domain.common.exception;

import com.example.lib.common.core.exception.BusinessBaseException;
import com.example.lib.web.core.exception.GlobalError;

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
