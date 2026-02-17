package me.vladislav.library_service_backend.auth.sheduler;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.repository.PasswordResetTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor

@Component
public class PasswordResetCleanupScheduler {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void cleanup() {
        passwordResetTokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
    }
}
