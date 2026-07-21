package com.example.modul3.controller;

import com.example.modul3.dto.AddProductToOrderRequestDto;
import com.example.modul3.dto.OrderItemResponseDto;
import com.example.modul3.dto.UpdateOrderItemQuantityRequestDto;
import com.example.modul3.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @PostMapping("/api/orders/{orderId}/items")
    public ResponseEntity<OrderItemResponseDto> addProductToOrder(@PathVariable Long orderId,@Valid @RequestBody AddProductToOrderRequestDto dto) {
        OrderItemResponseDto result = orderItemService.addProductToOrder(orderId, dto);
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/api/order-items/{id}")
    public ResponseEntity<OrderItemResponseDto> updateQuantity(@PathVariable Long id,@Valid @RequestBody UpdateOrderItemQuantityRequestDto dto) {
        OrderItemResponseDto result = orderItemService.updateQuantity(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/order-items/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/orders/{orderId}/items")
    public ResponseEntity<List<OrderItemResponseDto>> getItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemResponseDto> result = orderItemService.getItemsByOrderId(orderId);
        return ResponseEntity.ok(result);
    }


}
