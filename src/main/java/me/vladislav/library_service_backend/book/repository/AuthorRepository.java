package me.vladislav.library_service_backend.book.repository;

import me.vladislav.library_service_backend.book.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
