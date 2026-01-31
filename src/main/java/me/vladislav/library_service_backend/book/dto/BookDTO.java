package me.vladislav.library_service_backend.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String title;
    private Set<Long> authorIds;
    private Set<Long> libraryIds;
    private String publishingHouse;
    private LocalDate publicationYear;
    private String genre;
    private String language;
    private String isbn;
}
