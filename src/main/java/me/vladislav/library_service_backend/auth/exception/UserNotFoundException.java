package me.vladislav.library_service_backend.auth.exception;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String login) {
        super("A user with login \"" + login + "\" not found");
    }
}
