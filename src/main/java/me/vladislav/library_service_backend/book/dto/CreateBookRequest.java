package me.vladislav.library_service_backend.book.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequest {

    @NotBlank(message = "Название книги не может быть пустым")
    @Size(max = 255, message = "Название книги не должно превышать 255 символов")
    private String title;

    @NotEmpty(message = "У книги должен быть указан хотя бы один автор")
    private Set<Long> authorIds;

    @NotEmpty(message = "Необходимо указать хотя бы одну библиотеку")
    private Set<Long> libraryIds;

    @NotBlank(message = "Издательство не может быть пустым")
    @Size(max = 255, message = "Название издательства не должно превышать 255 символов")
    private String publishingHouse;

    @NotNull(message = "Год публикации обязателен")
    @PastOrPresent(message = "Год публикации не может быть в будущем")
    private LocalDate publicationYear;

    @NotBlank(message = "Жанр не может быть пустым")
    private String genre;

    @NotBlank(message = "Язык книги не может быть пустым")
    private String language;

    @NotBlank(message = "ISBN не может быть пустым")
    @Size(min = 10, max = 17, message = "ISBN должен содержать от 10 до 17 символов")
    private String isbn;

}

