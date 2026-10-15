package com.example.modul3.dto;

import com.example.modul3.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Ответ с данными сотрудника")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDto {

    @Schema(
            description = "Уникальный идентификатор сотрудника",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(description = "Имя сотрудника", example = "John")
    private String firstName;

    @Schema(description = "Фамилия сотрудника", example = "Doe")
    private String lastName;

    @Schema(description = "Email сотрудника", example = "john.doe@example.com")
    private String email;

    @Schema(
            description = "Роль сотрудника",
            allowableValues = {"MANAGER", "ADMIN"}
    )
    private Role role;
}