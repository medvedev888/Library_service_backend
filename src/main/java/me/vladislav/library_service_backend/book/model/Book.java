package me.vladislav.library_service_backend.book.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "books",
        indexes = {
                @Index(name = "books_isbn", columnList = "isbn", unique = true)
        })
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors;

    @ManyToMany
    @JoinTable(
            name = "book_libraries",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id")
    )
    private Set<Library> libraries;

    @Column(name = "publishing_house", nullable = false)
    private String publishingHouse;

    @Column(name = "publication_year", nullable = false)
    private LocalDate publicationYear;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false, length = 17, unique = true)
    private String isbn;

    @Column
    private Long copies;
}
