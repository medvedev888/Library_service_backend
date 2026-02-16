package me.vladislav.library_service_backend.fine.repository;

import me.vladislav.library_service_backend.fine.model.Fine;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    Optional<Fine> findByBookLoan(BookLoan bookLoan);

    @Query("select f from Fine f where f.bookLoan.user.id = :userId")
    List<Fine> findAllByUserId(@Param("userId") Long userId);
}
