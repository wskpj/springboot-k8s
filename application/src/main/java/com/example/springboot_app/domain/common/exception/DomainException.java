package com.example.springboot_app.domain.common.exception;

import com.example.lib.common.core.exception.BaseDomainException;
import com.example.lib.web.core.error.GlobalError;

public class DomainException {

    public static class RateLimit extends BaseDomainException {
        public RateLimit() {
            super(GlobalError.TOO_MANY_REQUESTS);
        }

        public RateLimit(Object details) {
            super(GlobalError.TOO_MANY_REQUESTS, details);
        }
    }
}
