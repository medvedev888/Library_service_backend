package me.vladislav.library_service_backend.book.exception;

import me.vladislav.library_service_backend.common.exception.AppException;
import org.springframework.http.HttpStatus;

public class InvalidResetTokenException extends AppException {

    public InvalidResetTokenException() {
        super(
                HttpStatus.BAD_REQUEST,
                "Недействительный токен сброса",
                "Токен недействителен, просрочен или уже использован"
        );
    }
}
