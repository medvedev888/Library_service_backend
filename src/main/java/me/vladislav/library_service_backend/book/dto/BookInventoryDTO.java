package me.vladislav.library_service_backend.book.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookInventoryDTO {
    private Long id;
    private Long bookId;
    private Long libraryId;
    private Long totalCopies;
    private Long availableCopies;
}
