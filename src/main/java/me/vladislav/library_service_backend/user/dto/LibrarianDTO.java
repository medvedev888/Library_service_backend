package me.vladislav.library_service_backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibrarianDTO {
    private Long id;
    private Long userId;
    private Long libraryId;
}
