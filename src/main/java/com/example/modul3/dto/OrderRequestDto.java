package com.example.modul3.dto;


import com.example.modul3.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderRequestDto {
    private OrderStatus status;
    private Long clientId;
}
