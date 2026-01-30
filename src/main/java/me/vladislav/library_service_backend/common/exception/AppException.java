package me.vladislav.library_service_backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AppException extends RuntimeException {

    private final HttpStatus status;
    private final String error;

    protected AppException(HttpStatus status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }
}