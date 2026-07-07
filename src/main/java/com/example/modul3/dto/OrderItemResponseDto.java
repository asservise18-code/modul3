package com.example.modul3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
}