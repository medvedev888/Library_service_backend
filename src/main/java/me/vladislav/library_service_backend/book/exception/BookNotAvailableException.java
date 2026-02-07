package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class BookNotAvailableException extends BookInventoryException {

    public BookNotAvailableException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

}
