package com.example.modul3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Ответ с данными товара")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    @Schema(
            description = "Уникальный идентификатор товара",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(description = "Название товара", example = "Laptop")
    private String name;

    @Schema(description = "Описание товара", example = "Ноутбук с 16 ГБ оперативной памяти")
    private String description;

    @Schema(description = "Цена товара", example = "1499.99")
    private BigDecimal price;
}