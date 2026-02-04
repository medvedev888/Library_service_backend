package me.vladislav.library_service_backend.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super(HttpStatus.BAD_REQUEST, "Неверный email или пароль");
    }
}