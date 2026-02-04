package me.vladislav.library_service_backend.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookInventoryRequest {

    @NotNull(message = "totalCopies обязателен")
    @Min(value = 0, message = "Количество экземпляров не может быть отрицательным")
    private Long totalCopies;

    @NotNull(message = "availableCopies обязателен")
    @Min(value = 0, message = "Доступное количество не может быть отрицательным")
    private Long availableCopies;

}
