package me.vladislav.library_service_backend.loan.exception;

import org.springframework.http.HttpStatus;

public class LibraryReferenceNotFoundException extends BookLoanException {

    public LibraryReferenceNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Библиотека с id " + id + " не найдена"
        );
    }

}
