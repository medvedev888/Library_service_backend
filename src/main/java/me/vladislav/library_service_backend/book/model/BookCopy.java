package me.vladislav.library_service_backend.book.model;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;
import me.vladislav.library_service_backend.library.model.Library;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "book_copies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_book_library",
                        columnNames = {"book_id", "library_id"}
                )
        }
)
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;

    @Column(nullable = false)
    private Long totalCopies;

    @Column(nullable = false)
    private Long availableCopies;
}

