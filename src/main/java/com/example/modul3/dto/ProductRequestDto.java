package com.example.modul3.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание или обновление товара")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @Schema(description = "Название товара", example = "Laptop")
    @NotBlank(message = "Название товара не может быть пустым")
    @Size(max = 100, message = "Название товара не может содержать более 100 символов")
    private String name;

    @Schema(description = "Описание товара", example = "Ноутбук с 16 ГБ оперативной памяти")
    @Size(max = 500, message = "Описание товара не может содержать более 500 символов")
    private String description;

    @Schema(description = "Цена товара", example = "1499.99")
    @NotNull(message = "Цена товара не может быть пустой")
    @Positive(message = "Цена товара должна быть больше 0")
    @DecimalMin(value = "0.01", message = "Цена товара должна быть не меньше 0.01")
    private BigDecimal price;
}