package com.example.modul3.dto;

import com.example.modul3.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Запрос на создание или обновление заказа")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @Schema(
            description = "Статус заказа",
            example = "NEW",
            allowableValues = {"NEW", "PROCESSING", "COMPLETED", "CANCELED"}
    )
    @NotNull(message = "Статус заказа не может быть пустым")
    private OrderStatus status;

    @Schema(description = "Идентификатор клиента", example = "1")
    @NotNull(message = "Идентификатор клиента не может быть пустым")
    private Long clientId;
}