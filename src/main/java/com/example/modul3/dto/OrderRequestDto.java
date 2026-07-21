package com.example.modul3.dto;


import com.example.modul3.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderRequestDto {
    @NotNull
    private OrderStatus status;

    @NotNull
    private Long clientId;
}
