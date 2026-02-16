package me.vladislav.library_service_backend.auth.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.model.PasswordResetToken;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.auth.repository.PasswordResetTokenRepository;
import me.vladislav.library_service_backend.auth.repository.UserRepository;
import me.vladislav.library_service_backend.common.mail.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

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

    @Transactional
    public void requestReset(String email) {
        // ВАЖНО: всегда отвечаем одинаково (чтобы не было user enumeration)
        var userOpt = userRepository.getUserByEmail(email.trim().toLowerCase());
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();

        String token = generateToken();
        LocalDateTime now = LocalDateTime.now();

        PasswordResetToken prt = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .createdAt(now)
                .expiresAt(now.plusMinutes(ttlMinutes))
                .build();

        tokenRepository.save(prt);

        String link = frontendBaseUrl + "/auth/reset-password?token=" + token;
        mailService.sendPasswordResetEmail(user.getEmail(), link);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_OR_EXPIRED_TOKEN"));

        if (prt.getUsedAt() != null) {
            throw new IllegalArgumentException("INVALID_OR_EXPIRED_TOKEN");
        }

        if (prt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("INVALID_OR_EXPIRED_TOKEN");
        }

        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        prt.setUsedAt(LocalDateTime.now());
        tokenRepository.save(prt);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        RNG.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
