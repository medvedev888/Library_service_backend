package me.vladislav.library_service_backend.auth.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String email) {
        super(HttpStatus.CONFLICT, "Пользователь с email \"" + email + "\" уже существует");
    }
}
