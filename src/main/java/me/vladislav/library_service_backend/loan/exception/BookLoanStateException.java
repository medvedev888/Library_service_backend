package me.vladislav.library_service_backend.loan.exception;

import org.springframework.http.HttpStatus;

public class BookLoanStateException extends BookLoanException {

    public BookLoanStateException(HttpStatus status, String message) {
        super(status, message);
    }

}
