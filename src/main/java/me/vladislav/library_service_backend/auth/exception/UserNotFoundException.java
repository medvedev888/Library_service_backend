package me.vladislav.library_service_backend.auth.exception;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String email) {
        super("A user with email \"" + email + "\" not found");
    }
}
