package me.vladislav.library_service_backend.auth.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.model.PasswordResetToken;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.repository.PasswordResetTokenRepository;
import me.vladislav.library_service_backend.auth.repository.UserRepository;
import me.vladislav.library_service_backend.book.exception.InvalidResetTokenException;
import me.vladislav.library_service_backend.common.mail.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;

@RequiredArgsConstructor

@Service
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${library.password-reset.token-ttl-minutes:15}")
    private long ttlMinutes;

    private static final SecureRandom RNG = new SecureRandom();
    private static final int TOKEN_LENGTH = 32;


    @Transactional
    public void requestReset(String email) {
        var userOpt = userRepository.getUserByEmail(email.trim().toLowerCase());

        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();

        tokenRepository.deleteAllByUserId(user.getId());

        String rawToken = generateToken();
        String tokenHash = passwordEncoder.encode(rawToken);
        LocalDateTime now = LocalDateTime.now();
        PasswordResetToken token = PasswordResetToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .createdAt(now)
                .expiresAt(now.plusMinutes(ttlMinutes))
                .build();

        tokenRepository.save(token);

        String link = frontendBaseUrl + "/auth/reset-password?token=" + rawToken;
        mailService.sendPasswordResetEmail(user.getEmail(), link);
    }


    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        if (rawToken == null || rawToken.isBlank()) {
            throw new InvalidResetTokenException();
        }

        List<PasswordResetToken> tokens =
                tokenRepository.findAllByUsedAtIsNullAndExpiresAtAfter(LocalDateTime.now());
        PasswordResetToken validToken = null;

        for (PasswordResetToken token : tokens) {
            if (token.getUsedAt() != null) {
                continue;
            }
            if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
                continue;
            }
            if (passwordEncoder.matches(rawToken, token.getTokenHash())) {
                validToken = token;
                break;
            }
        }

        if (validToken == null) {
            throw new InvalidResetTokenException();
        }

        User user = validToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        validToken.setUsedAt(LocalDateTime.now());
        tokenRepository.save(validToken);
        tokenRepository.deleteAllByUserId(user.getId());
    }

    private String generateToken() {
        byte[] bytes = new byte[TOKEN_LENGTH];
        RNG.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
