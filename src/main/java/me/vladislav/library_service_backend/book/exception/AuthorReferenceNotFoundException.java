package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class AuthorReferenceNotFoundException extends BookException {
    public AuthorReferenceNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Автор с id " + id + " не найден"
        );
    }

    public AuthorReferenceNotFoundException() {
        super(
                HttpStatus.BAD_REQUEST,
                "Один или несколько авторов не найдено"
        );
    }
}
