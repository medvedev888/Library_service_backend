package me.vladislav.library_service_backend.auth.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.dto.UserDTO;
import me.vladislav.library_service_backend.auth.exception.UserAlreadyExistsException;
import me.vladislav.library_service_backend.auth.exception.UserNotFoundException;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(UserDTO userDTO) {
        User user = new User(userDTO.login(), userDTO.password(), userDTO.role());
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new UserAlreadyExistsException(user.getLogin());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getByLogin(String login) {
        return userRepository.getUserByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public User getCurrentUser() {
        var login = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByLogin(login);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}