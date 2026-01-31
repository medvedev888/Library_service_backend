package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class AuthorNotFoundException extends BookException {
    public AuthorNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Автор с id " + id + " не найден"
        );
    }

    public AuthorNotFoundException() {
        super(
                HttpStatus.BAD_REQUEST,
                "Один или несколько авторов не найдено"
        );
    }
}
