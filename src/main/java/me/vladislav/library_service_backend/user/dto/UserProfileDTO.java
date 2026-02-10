package me.vladislav.library_service_backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String avatarUrl;
}
