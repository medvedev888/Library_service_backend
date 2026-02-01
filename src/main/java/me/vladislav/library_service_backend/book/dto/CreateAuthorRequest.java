package me.vladislav.library_service_backend.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthorRequest {
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 255, message = "Фамилия не должна превышать 255 символов")
    private String surname;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 255, message = "Имя не должно превышать 255 символов")
    private String name;

    @Size(max = 255, message = "Отчество не должно превышать 255 символов")
    private String middleName;
}
