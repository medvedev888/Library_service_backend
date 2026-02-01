package me.vladislav.library_service_backend.library.model;

import jakarta.persistence.*;
import lombok.*;
import me.vladislav.library_service_backend.book.model.Book;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "libraries",
        indexes = {
                @Index(name = "libraries_address", columnList = "address", unique = true)
        })
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "jsonb", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> address;

    @Column(name = "staff_number", nullable = false)
    private Long staffNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LibraryStatus status;

    @ManyToMany(mappedBy = "libraries")
    private Set<Book> books;
}
