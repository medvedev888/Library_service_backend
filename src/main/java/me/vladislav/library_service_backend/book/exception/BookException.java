package me.vladislav.library_service_backend.book.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;

public abstract class BookException extends AppException {

    protected BookException(HttpStatus status, String message) {
        super(status, "Ошибка работы с книгой", message);
    }
}
