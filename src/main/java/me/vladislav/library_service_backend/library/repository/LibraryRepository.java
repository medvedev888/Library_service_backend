package me.vladislav.library_service_backend.library.repository;

import me.vladislav.library_service_backend.library.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
