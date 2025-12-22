package com.example.springboot_app.domain.auth.exception;

import com.example.lib.common.core.exception.BusinessBaseException;
import com.example.lib.web.core.exception.GlobalError;

public class AuthException {

    public static class Unauthorized extends BusinessBaseException {
        public Unauthorized() {
            super(GlobalError.UNAUTHORIZED, "Authentication is required.");
        }

        public Unauthorized(String message) {
            super(GlobalError.UNAUTHORIZED, message);
        }
    }

    public static class InvalidToken extends BusinessBaseException {
        public InvalidToken() {
            super(GlobalError.UNAUTHORIZED, "Invalid or expired token.");
        }
    }

    public static class InvalidCredentials extends BusinessBaseException {
        public InvalidCredentials() {
            super(GlobalError.UNAUTHORIZED, "Invalid email or password.");
        }
    }

    public static class EmailAlreadyExists extends BusinessBaseException {
        public EmailAlreadyExists(String email) {
            super(GlobalError.CONFLICT, "Email [" + email + "] already exists.");
        }
    }

    public static class TooManyLoginAttempts extends BusinessBaseException {
        public TooManyLoginAttempts() {
            super(GlobalError.TOO_MANY_REQUESTS, "Too many login attempts. Account temporarily locked.");
        }
    }

    public static class AccessDenied extends BusinessBaseException {
        public AccessDenied() {
            super(GlobalError.FORBIDDEN, "Access denied.");
        }

        public AccessDenied(String message) {
            super(GlobalError.FORBIDDEN, message);
        }
    }
}
