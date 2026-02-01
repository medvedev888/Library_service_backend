package me.vladislav.library_service_backend.book.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;

public abstract class AuthorException extends AppException {

    protected AuthorException(HttpStatus status, String message) {
        super(status, "Ошибка работы с автором", message);
    }

}