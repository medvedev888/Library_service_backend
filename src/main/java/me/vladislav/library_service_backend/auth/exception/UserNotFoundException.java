package me.vladislav.library_service_backend.auth.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AuthException {

    public UserNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "Пользователь с email \"" + email + "\" не найден");
    }

    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Пользователь с id \"" + id + "\" не найден");
    }

}