package com.example.modul3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с данными позиции заказа")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    @Schema(
            description = "Уникальный идентификатор позиции заказа",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Идентификатор заказа, к которому относится позиция",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long orderId;

    @Schema(
            description = "Идентификатор товара в позиции заказа",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long productId;

    @Schema(
            description = "Название товара",
            example = "Laptop",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String productName;

    @Schema(
            description = "Количество товара в позиции заказа",
            example = "2",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer quantity;
}