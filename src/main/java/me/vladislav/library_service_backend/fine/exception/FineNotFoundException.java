package me.vladislav.library_service_backend.fine.exception;

import org.springframework.http.HttpStatus;

public class FineNotFoundException extends FineException {
    public FineNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Штраф с id " + id + " не найден");
    }
}
