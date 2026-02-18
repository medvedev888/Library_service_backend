package me.vladislav.library_service_backend.user.exception;

import org.springframework.http.HttpStatus;

public class LibrarianNotFoundException extends LibrarianException {

    public LibrarianNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Библиотекарь с id " + id + " не найден"
        );
    }

}
