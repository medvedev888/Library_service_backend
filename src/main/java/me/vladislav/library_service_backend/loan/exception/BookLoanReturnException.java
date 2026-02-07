package me.vladislav.library_service_backend.loan.exception;

import org.springframework.http.HttpStatus;

public class BookLoanReturnException extends BookLoanException {

    public BookLoanReturnException(HttpStatus status, String message) {
        super(status, message);
    }

}
