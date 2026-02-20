package me.vladislav.library_service_backend.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookInventoryRequest {
    @NotNull(message = "bookId обязателен")
    private Long bookId;

    @NotNull(message = "libraryId обязателен")
    private Long libraryId;

    @NotNull(message = "totalCopies обязателен")
    @Min(value = 0, message = "Количество экземпляров не может быть отрицательным")
    private Long totalCopies;

    @NotNull(message = "availableCopies обязателен")
    @Min(value = 0, message = "Доступное количество не может быть отрицательным")
    private Long availableCopies;
}