package me.vladislav.library_service_backend.book.repository;

import me.vladislav.library_service_backend.book.model.BookInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookInventoryRepository extends JpaRepository<BookInventory, Long>, JpaSpecificationExecutor<BookInventory> {
    Optional<BookInventory> findByBookIdAndLibraryId(Long bookId, Long libraryId);

    boolean existsByBookIdAndLibraryId(Long bookId, Long libraryId);
}