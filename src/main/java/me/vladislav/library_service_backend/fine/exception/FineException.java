package me.vladislav.library_service_backend.fine.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;

public class FineException extends AppException {
    protected FineException(HttpStatus status, String message) {
        super(status, "Ошибка работы со штрафами", message);
    }
}
