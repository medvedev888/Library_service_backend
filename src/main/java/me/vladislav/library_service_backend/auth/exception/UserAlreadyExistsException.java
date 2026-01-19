package me.vladislav.library_service_backend.auth.exception;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String login) {
        super("A user with login \"" + login + "\" already exists");
    }
}
