package me.vladislav.library_service_backend.library.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;

public class LibraryException extends AppException {

    protected LibraryException(HttpStatus status, String message) {
        super(status, "Ошибка работы с библиотекой", message);
    }

}