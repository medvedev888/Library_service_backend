package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class DuplicateBookInventoryException extends BookInventoryException {

    public DuplicateBookInventoryException(Long bookId, Long libraryId) {
        super(
                HttpStatus.CONFLICT,
                "Инвентарь книги с id книги " + bookId + " и id библиотеки " + libraryId + " уже существует"
        );
    }
}
