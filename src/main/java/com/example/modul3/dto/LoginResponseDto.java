package com.example.modul3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Ответ с JWT-токеном после успешной аутентификации")
@Getter
@AllArgsConstructor
public class LoginResponseDto {

    @Schema(
            description = "JWT access-токен",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9.signature",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String token;
}