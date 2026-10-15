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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Order Item Controller",
        description = "Контроллер для управления позициями заказа"
)
@RestController
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавление товара в заказ",
            description = "Позволяет добавить товар в заказ или увеличить количество, если товар уже есть в заказе"
    )
    @ApiResponse(responseCode = "201", description = "Товар добавлен в заказ")
    @ApiResponse(responseCode = "400", description = "Некорректное количество товара")
    @ApiResponse(responseCode = "404", description = "Заказ или товар не найден")
    @PostMapping("/api/orders/{orderId}/items")
    public ResponseEntity<OrderItemResponseDto> addProductToOrder(
            @PathVariable
            @Parameter(description = "Идентификатор заказа", required = true)
            Long orderId,

            @Valid @RequestBody AddProductToOrderRequestDto dto) {
        OrderItemResponseDto result = orderItemService.addProductToOrder(orderId, dto);
        return ResponseEntity.status(201).body(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Обновление количества позиции заказа",
            description = "Позволяет изменить количество товара в позиции заказа"
    )
    @ApiResponse(responseCode = "200", description = "Количество позиции заказа обновлено")
    @ApiResponse(responseCode = "404", description = "Позиция заказа не найдена")
    @PutMapping("/api/order-items/{id}")
    public ResponseEntity<OrderItemResponseDto> updateQuantity(
            @PathVariable
            @Parameter(description = "Идентификатор позиции заказа", required = true)
            Long id,

            @Valid @RequestBody UpdateOrderItemQuantityRequestDto dto) {
        OrderItemResponseDto result = orderItemService.updateQuantity(id, dto);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление позиции заказа",
            description = "Позволяет удалить позицию заказа по её идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Позиция заказа удалена")
    @ApiResponse(responseCode = "404", description = "Позиция заказа не найдена")
    @DeleteMapping("/api/order-items/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Parameter(description = "Идентификатор позиции заказа", required = true)
            Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение позиций заказа",
            description = "Позволяет получить все позиции конкретного заказа"
    )
    @ApiResponse(responseCode = "200", description = "Список позиций заказа получен")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @GetMapping("/api/orders/{orderId}/items")
    public ResponseEntity<List<OrderItemResponseDto>> getItemsByOrderId(
            @PathVariable
            @Parameter(description = "Идентификатор заказа", required = true)
            Long orderId) {
        List<OrderItemResponseDto> result = orderItemService.getItemsByOrderId(orderId);
        return ResponseEntity.ok(result);
    }
}