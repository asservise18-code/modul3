package com.example.modul3.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на аутентификацию сотрудника")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(description = "Email сотрудника", example = "john.doe@example.com")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @Schema(description = "Пароль сотрудника", example = "Password123")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}