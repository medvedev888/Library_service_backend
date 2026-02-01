package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class BookReferenceNotFoundException extends AuthorException {
    public BookReferenceNotFoundException() {
        super(
                HttpStatus.BAD_REQUEST,
                "Одна или несколько книг не найдено"
        );
    }
}
