package me.vladislav.library_service_backend.loan.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;

public class BookLoanException extends AppException {

    protected BookLoanException(HttpStatus status, String message) {
        super(status, "Ошибка процесса бронирования или выдачи книги", message);
    }

}
