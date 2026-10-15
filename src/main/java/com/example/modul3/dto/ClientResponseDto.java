package com.example.modul3.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с данными клиента")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponseDto {

    @Schema(
            description = "Уникальный идентификатор клиента",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(description = "Имя клиента", example = "Alice")
    private String firstName;

    @Schema(description = "Фамилия клиента", example = "Smith")
    private String lastName;

    @Schema(description = "Email клиента", example = "alice.smith@example.com")
    private String email;

    @Schema(description = "Номер телефона клиента", example = "+994501234567")
    private String phone;

    @Schema(
            description = "Список заказов клиента",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private List<OrderResponseDto> orders;
}