package me.vladislav.library_service_backend.fine.repository;

import me.vladislav.library_service_backend.fine.model.Fine;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    Optional<Fine> findByBookLoan(BookLoan bookLoan);
}
