package me.vladislav.library_service_backend.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends AppException {

    public InvalidParameterException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                "Невалидный параметр",
                message
        );
    }
}