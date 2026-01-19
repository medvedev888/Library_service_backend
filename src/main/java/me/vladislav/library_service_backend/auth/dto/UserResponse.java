package me.vladislav.library_service_backend.auth.dto;

import me.vladislav.library_service_backend.auth.model.Role;

public record UserResponse (String login, Role role){}
