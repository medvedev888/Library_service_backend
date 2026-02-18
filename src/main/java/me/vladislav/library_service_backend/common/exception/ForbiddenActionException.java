package me.vladislav.library_service_backend.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenActionException extends AppException {

    public ForbiddenActionException(String message) {
        super(
                HttpStatus.FORBIDDEN,
                "Недостаточно прав",
                message
        );
    }
}