package me.vladislav.library_service_backend.auth.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.dto.SignInRequest;
import me.vladislav.library_service_backend.auth.dto.SignUpRequest;
import me.vladislav.library_service_backend.auth.dto.UserDTO;
import me.vladislav.library_service_backend.auth.exception.InvalidCredentialsException;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;

    public String register(SignUpRequest request) {
        UserDTO userDTO = new UserDTO(request.getEmail(), request.getPassword(), request.getRole());
        User user = userService.create(userDTO);
        return jwtService.generateToken(new CustomUserDetails(user));
    }

    public String login(SignInRequest request) {
        User user = userService.getByEmail(request.getEmail());
        if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return jwtService.generateToken(new CustomUserDetails(user));
    }
}
