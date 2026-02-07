package me.vladislav.library_service_backend.loan.exception;

import org.springframework.http.HttpStatus;

public class BookLoanReservationException extends BookLoanException {

    public BookLoanReservationException(HttpStatus status, String message) {
        super(status, message);
    }

}
