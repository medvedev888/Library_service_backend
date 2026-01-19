package me.vladislav.library_service_backend.auth.repository;

import me.vladislav.library_service_backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByLogin(String email);
    boolean existsByLogin(String email);
}
