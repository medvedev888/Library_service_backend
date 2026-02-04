package me.vladislav.library_service_backend.book.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;

public class BookInventoryException extends AppException {
    protected BookInventoryException(HttpStatus status, String message) {
        super(status, "Ошибка работы с инвентарём книг", message);
    }
}
