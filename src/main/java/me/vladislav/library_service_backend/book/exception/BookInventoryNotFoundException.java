package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class BookInventoryNotFoundException extends BookInventoryException {
    public BookInventoryNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Инвентарь книги с id " + id + " не найден"
        );
    }
}
