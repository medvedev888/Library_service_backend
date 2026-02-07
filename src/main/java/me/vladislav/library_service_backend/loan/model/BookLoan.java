package me.vladislav.library_service_backend.loan.model;

import jakarta.persistence.*;
import lombok.*;
import me.vladislav.library_service_backend.auth.model.User;
import me.vladislav.library_service_backend.book.model.Book;
import me.vladislav.library_service_backend.library.model.Library;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "book_loans")
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Library library;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    private LocalDateTime reservedAt;
    private LocalDateTime reservedUntil;

    private LocalDateTime issuedAt;
    private LocalDateTime dueAt;

    private LocalDateTime returnedAt;
}
