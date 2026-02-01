package me.vladislav.library_service_backend.book.repository;

import me.vladislav.library_service_backend.book.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {
    boolean existsBySurnameAndNameAndMiddleName(String surname, String name, String middleName);
}
