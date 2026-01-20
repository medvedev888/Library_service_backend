package me.vladislav.library_service_backend.auth.exception;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String email) {
        super("A user with email \"" + email + "\" already exists");
    }
}
