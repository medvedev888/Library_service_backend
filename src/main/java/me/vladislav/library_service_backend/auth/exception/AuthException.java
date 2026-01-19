package me.vladislav.library_service_backend.auth.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super("Authorization error: " + message);
    }
}
