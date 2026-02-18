package me.vladislav.library_service_backend.auth.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.dto.SignInRequest;
import me.vladislav.library_service_backend.auth.dto.SignUpRequest;
import me.vladislav.library_service_backend.auth.dto.UserDTO;
import me.vladislav.library_service_backend.auth.exception.InvalidCredentialsException;
import me.vladislav.library_service_backend.auth.model.Role;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import me.vladislav.library_service_backend.user.dto.LibrarianDTO;
import me.vladislav.library_service_backend.user.dto.UserProfileDTO;
import me.vladislav.library_service_backend.user.service.LibrarianService;
import me.vladislav.library_service_backend.user.service.UserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserProfileService userProfileService;
    private final LibrarianService librarianService;

    @Transactional
    public String register(SignUpRequest request) {
        UserDTO userDTO = new UserDTO(request.getEmail(), request.getPassword(), request.getRole());
        User user = userService.create(userDTO);

        UserProfileDTO userProfileDTO = UserProfileDTO.builder().userId(user.getId()).build();
        userProfileService.create(userProfileDTO);

        if (user.getRole().equals(Role.LIBRARIAN)) {
            LibrarianDTO librarianDTO = LibrarianDTO.builder().userId(user.getId()).build();
            librarianService.create(librarianDTO);
        }

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
