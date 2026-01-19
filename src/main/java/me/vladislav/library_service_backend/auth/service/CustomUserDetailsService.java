package me.vladislav.library_service_backend.auth.service;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.getUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User with login: '" + login + "' not found"));
        return new CustomUserDetails(user);
    }

}