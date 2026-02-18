package me.vladislav.library_service_backend.user.model;

import jakarta.persistence.*;
import lombok.*;
import me.vladislav.library_service_backend.library.model.Library;
import me.vladislav.library_service_backend.auth.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "librarians")
public class Librarian {
    @Id
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;
}
