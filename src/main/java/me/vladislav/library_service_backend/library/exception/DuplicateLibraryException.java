package me.vladislav.library_service_backend.library.exception;

import org.springframework.http.HttpStatus;

public class DuplicateLibraryException extends LibraryException {

    public DuplicateLibraryException(String address) {
        super(
                HttpStatus.CONFLICT,
                "Библиотека с адресом " + address + " уже существует"
        );
    }
}
