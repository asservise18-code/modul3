package com.example.modul3.dto;

import com.example.modul3.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Ответ с данными заказа")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {

    @Schema(
            description = "Уникальный идентификатор заказа",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Дата и время создания заказа",
            example = "2026-10-15T00:14:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Статус заказа",
            example = "NEW",
            allowableValues = {"NEW", "PROCESSING", "COMPLETED", "CANCELED"},
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private OrderStatus status;

    @Schema(
            description = "Идентификатор клиента, оформившего заказ",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long clientId;

    @Schema(
            description = "Полное имя клиента",
            example = "Alice Smith",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String clientFullName;
}