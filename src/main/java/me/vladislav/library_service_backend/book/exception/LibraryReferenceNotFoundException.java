package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class LibraryReferenceNotFoundException extends BookException {
    public LibraryReferenceNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Библиотека с id " + id + " не найдена"
        );
    }

    public LibraryReferenceNotFoundException() {
        super(
                HttpStatus.BAD_REQUEST,
                "Одна или несколько библиотек не найдены"
        );
    }
}
