package me.vladislav.library_service_backend.loan.exception;

import org.springframework.http.HttpStatus;

public class BookReferenceNotFoundException extends BookLoanException {

    public BookReferenceNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Книга с id " + id + " не найдена"
        );
    }

}
