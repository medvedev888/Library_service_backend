package me.vladislav.library_service_backend.loan.exception;

import org.springframework.http.HttpStatus;

public class BookLoanNotFoundException extends BookLoanException {

    public BookLoanNotFoundException(Long id) {
        super(
                HttpStatus.NOT_FOUND,
                "Запись бронирования с id " + id + " не найдена"
        );
    }

}
