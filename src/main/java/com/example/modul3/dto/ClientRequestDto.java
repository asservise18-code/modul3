package com.example.modul3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Запрос на создание или обновление клиента")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDto {

    @Schema(description = "Имя клиента", example = "Alice")
    @NotBlank(message = "Имя не может быть пустым")
    private String firstName;

    @Schema(description = "Фамилия клиента", example = "Smith")
    @NotBlank(message = "Фамилия не может быть пустой")
    private String lastName;

    @Schema(description = "Email клиента", example = "alice.smith@example.com")
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

    @Schema(description = "Номер телефона клиента", example = "+994501234567")
    @NotBlank(message = "Номер телефона не может быть пустым")
    @Size(min = 9, max = 20, message = "Номер телефона должен содержать от 9 до 20 символов")
    private String phone;
}