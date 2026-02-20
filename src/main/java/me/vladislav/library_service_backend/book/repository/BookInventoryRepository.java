package me.vladislav.library_service_backend.book.repository;

import jakarta.persistence.LockModeType;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.book.model.BookInventory;
import me.vladislav.library_service_backend.library.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface BookInventoryRepository extends JpaRepository<BookInventory, Long>, JpaSpecificationExecutor<BookInventory> {
    Optional<BookInventory> findByBookIdAndLibraryId(Long bookId, Long libraryId);

    boolean existsByBookIdAndLibraryId(Long bookId, Long libraryId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BookInventory> findByBookAndLibrary(Book book, Library library);
}