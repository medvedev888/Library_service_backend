package me.vladislav.library_service_backend.library.repository;

import me.vladislav.library_service_backend.library.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;

public interface LibraryRepository extends JpaRepository<Library, Long>, JpaSpecificationExecutor<Library> {
    boolean existsByAddress(Map<String, Object> address);
}
