package me.vladislav.library_service_backend.auth.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.exception.UserNotFoundException;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.repository.UserRepository;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with login: '" + email + "' not found"));
        return new CustomUserDetails(user);
    }

}