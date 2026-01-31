package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class LibraryNotFoundException extends BookException {
    public LibraryNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Библиотека с id " + id + " не найдена"
        );
    }

    public LibraryNotFoundException() {
        super(
                HttpStatus.BAD_REQUEST,
                "Одна или несколько библиотек не найдены"
        );
    }
}
