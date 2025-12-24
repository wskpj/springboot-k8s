package com.example.springboot_app.domain.auth.exception;

import com.example.lib.common.core.exception.BaseDomainException;
import com.example.lib.web.core.error.GlobalError;

public class AuthException {

    public static class Unauthorized extends BaseDomainException {
        public Unauthorized() {
            super(GlobalError.UNAUTHORIZED, "Authentication is required.");
        }

        public Unauthorized(String message) {
            super(GlobalError.UNAUTHORIZED, message);
        }
    }

    public static class InvalidToken extends BaseDomainException {
        public InvalidToken() {
            super(GlobalError.UNAUTHORIZED, "Invalid or expired token.");
        }
    }

    public static class InvalidCredentials extends BaseDomainException {
        public InvalidCredentials() {
            super(GlobalError.UNAUTHORIZED, "Invalid email or password.");
        }
    }

    public static class EmailAlreadyExists extends BaseDomainException {
        public EmailAlreadyExists(String email) {
            super(GlobalError.CONFLICT, "Email [" + email + "] already exists.");
        }
    }

    public static class TooManyLoginAttempts extends BaseDomainException {
        public TooManyLoginAttempts() {
            super(GlobalError.TOO_MANY_REQUESTS, "Too many login attempts. Account temporarily locked.");
        }
    }

    public static class AccessDenied extends BaseDomainException {
        public AccessDenied() {
            super(GlobalError.FORBIDDEN, "Access denied.");
        }

        public AccessDenied(String message) {
            super(GlobalError.FORBIDDEN, message);
        }
    }
}
