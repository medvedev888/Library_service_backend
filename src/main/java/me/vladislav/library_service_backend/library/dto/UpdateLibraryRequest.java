package me.vladislav.library_service_backend.library.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vladislav.library_service_backend.library.model.LibraryStatus;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLibraryRequest {

    @NotNull(message = "Адрес не может быть null")
    private Map<String, Object> address;

    @NotNull(message = "Количество сотрудников обязательно")
    @PositiveOrZero(message = "Количество сотрудников не может быть отрицательным")
    private Long staffNumber;

    @NotNull(message = "Статус библиотеки обязателен")
    private LibraryStatus status;
}