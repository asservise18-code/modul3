package com.example.modul3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Запрос на обновление количества позиции заказа")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderItemQuantityRequestDto {

    @Schema(description = "Новое количество товара в позиции заказа", example = "3")
    @NotNull(message = "Количество товара не может быть пустым")
    @Min(value = 1, message = "Количество товара должно быть минимум 1")
    private Integer quantity;
}