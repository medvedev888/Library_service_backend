package me.vladislav.library_service_backend.book.repository;

import me.vladislav.library_service_backend.book.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
