package me.vladislav.library_service_backend.auth.exception;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super("Incorrect email or password");
    }
}
