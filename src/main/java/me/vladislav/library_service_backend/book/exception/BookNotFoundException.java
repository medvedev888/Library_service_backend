package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends BookException {
    public BookNotFoundException(Long id) {
        super(
                HttpStatus.NOT_FOUND,
                "Книга с id " + id + " не найдена"
        );
    }

    public BookNotFoundException(String isbn) {
        super(
                HttpStatus.NOT_FOUND,
                "Книга с isbn " + isbn + " не найдена"
        );
    }
}
