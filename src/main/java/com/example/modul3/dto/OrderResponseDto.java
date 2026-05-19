package com.example.modul3.dto;


import com.example.modul3.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private Long clientId;
    private String clientFullName;
}
