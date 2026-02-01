package me.vladislav.library_service_backend.book.exception;

import org.springframework.http.HttpStatus;

public class DuplicateAuthorException extends AuthorException {
    public DuplicateAuthorException(String surname, String name, String middleName) {
        super(
                HttpStatus.CONFLICT,
                "Автор " + name + " " + middleName + " " + surname + " уже существует"
        );
    }
}