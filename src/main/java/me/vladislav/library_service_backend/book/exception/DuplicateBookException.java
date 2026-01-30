package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class DuplicateBookException extends BookException {
    public DuplicateBookException(String isbn) {
        super(
                HttpStatus.CONFLICT,
                "Книга с ISBN " + isbn + " уже существует"
        );
    }
}
