package com.example.modul3.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProductToOrderRequestDto {
    @NotNull
    private Long productId;

    @Min(1)
    @NotNull
    private Integer quantity;
}
