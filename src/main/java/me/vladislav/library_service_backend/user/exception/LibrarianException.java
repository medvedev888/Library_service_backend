package me.vladislav.library_service_backend.user.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;


public class LibrarianException extends AppException {

    protected LibrarianException(HttpStatus status, String message) {
        super(status, "Ошибка работы с библиотекарем", message);
    }

}
