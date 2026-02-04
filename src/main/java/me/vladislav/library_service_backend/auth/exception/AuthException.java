package me.vladislav.library_service_backend.auth.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;

public class AuthException extends AppException {
    protected AuthException(HttpStatus status, String message) {
        super(status, "Ошибка авторизации", message);
    }
}
