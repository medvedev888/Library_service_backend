package me.vladislav.library_service_backend.loan.exception;

import org.springframework.http.HttpStatus;

public class UserReferenceNotFoundException extends BookLoanException {

    public UserReferenceNotFoundException(Long id) {
        super(
                HttpStatus.BAD_REQUEST,
                "Пользователь с id " + id + " не найден"
        );
    }

}
