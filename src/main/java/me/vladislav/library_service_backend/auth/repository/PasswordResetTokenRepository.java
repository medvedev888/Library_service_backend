package me.vladislav.library_service_backend.auth.repository;

import me.vladislav.library_service_backend.auth.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUserIdAndUsedAtIsNull(Long userId);

    List<PasswordResetToken> findAllByUserIdAndUsedAtIsNull(Long userId);

    List<PasswordResetToken> findAllByUsedAtIsNullAndExpiresAtAfter(LocalDateTime now);

    void deleteAllByUserId(Long userId);

    void deleteAllByExpiresAtBefore(LocalDateTime time);
}
