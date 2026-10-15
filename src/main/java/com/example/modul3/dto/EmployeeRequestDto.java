package com.example.modul3.dto;

import com.example.modul3.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Запрос на создание или обновление сотрудника")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDto {

    @Schema(description = "Имя сотрудника", example = "John")
    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @Schema(description = "Фамилия сотрудника", example = "Doe")
    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;

    @Schema(description = "Email сотрудника", example = "john.doe@example.com")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @Schema(description = "Пароль сотрудника", example = "Password123")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @Schema(
            description = "Роль сотрудника",
            allowableValues = {"MANAGER", "ADMIN"}
    )
    @NotNull(message = "Роль не может быть пустой")
    private Role role;
}