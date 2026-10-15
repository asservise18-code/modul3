package com.example.modul3.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на добавление товара в заказ")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToOrderRequestDto {

    @Schema(description = "Идентификатор товара", example = "1")
    @NotNull(message = "Идентификатор товара не может быть пустым")
    private Long productId;

    @Schema(description = "Количество товара", example = "2")
    @NotNull(message = "Количество товара не может быть пустым")
    @Min(value = 1, message = "Количество товара должно быть минимум 1")
    private Integer quantity;
}