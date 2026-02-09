package me.vladislav.library_service_backend.loan.repository;

import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.library.model.Library;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import me.vladislav.library_service_backend.loan.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    boolean existsByUserAndBookAndLibraryAndStatusIn(User user, Book book, Library library, List<LoanStatus> status);

    List<BookLoan> findAllByStatusInAndDueAtBefore(List<LoanStatus> statuses, LocalDateTime now);
}
