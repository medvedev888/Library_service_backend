package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundInInventoryException extends BookInventoryException {

    public BookNotFoundInInventoryException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}

