package me.vladislav.library_service_backend.user.model;

import jakarta.persistence.*;
import lombok.*;
import me.vladislav.library_service_backend.auth.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private Long id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String lastName;

    private String firstName;

    private String middleName;

    private Integer age;

    private String avatarUrl;
}
