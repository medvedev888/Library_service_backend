package me.vladislav.library_service_backend.auth.dto;

import me.vladislav.library_service_backend.auth.model.Role;

public record UserDTO(String email, String password, Role role) {}
