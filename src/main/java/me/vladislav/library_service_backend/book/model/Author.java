package me.vladislav.library_service_backend.book.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "authors",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_author_fullname",
                        columnNames = {"surname", "name", "middle_name"}
                )
        }
)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;
}
