package me.vladislav.library_service_backend.user.repository;

import me.vladislav.library_service_backend.user.model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    Optional<Librarian> findByUserId(Long id);
}
