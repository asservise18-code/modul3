package com.example.modul3.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderItemQuantityRequestDto {

    @NotNull
    @Min(1)
    private Integer quantity;
}
