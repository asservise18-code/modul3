package com.example.modul3.controller;

import com.example.modul3.dto.OrderRequestDto;
import com.example.modul3.dto.OrderResponseDto;
import com.example.modul3.model.OrderStatus;
import com.example.modul3.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Order Controller",
        description = "Контроллер для управления заказами"
)
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @Operation(
            summary = "Создание заказа",
            description = "Позволяет создать новый заказ для клиента"
    )
    @ApiResponse(responseCode = "201", description = "Заказ создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @PostMapping
    public ResponseEntity<OrderResponseDto> create(
            @Valid @RequestBody OrderRequestDto dto) {
        OrderResponseDto result = service.create(dto);
        return ResponseEntity.status(201).body(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Обновление заказа",
            description = "Позволяет обновить статус заказа и клиента по идентификатору заказа"
    )
    @ApiResponse(responseCode = "200", description = "Заказ обновлён")
    @ApiResponse(responseCode = "404", description = "Заказ или клиент не найден")
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> update(
            @PathVariable
            @Parameter(description = "Идентификатор заказа", required = true)
            Long id,

            @Valid @RequestBody OrderRequestDto dto) {
        OrderResponseDto result = service.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление заказа",
            description = "Позволяет удалить заказ по его идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Заказ удалён")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Parameter(description = "Идентификатор заказа", required = true)
            Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение заказа по id",
            description = "Позволяет получить данные заказа по его идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Заказ найден")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(
            @PathVariable
            @Parameter(description = "Идентификатор заказа", required = true)
            Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение списка заказов",
            description = "Позволяет получить заказы с пагинацией, сортировкой и фильтрацией по статусу и дате создания"
    )
    @ApiResponse(responseCode = "200", description = "Список заказов получен")
    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getAll(
            @RequestParam(defaultValue = "0")
            @Parameter(description = "Номер страницы", example = "0")
            int page,

            @RequestParam(defaultValue = "10")
            @Parameter(description = "Количество заказов на странице", example = "10")
            int size,

            @RequestParam(defaultValue = "asc")
            @Parameter(description = "Направление сортировки по дате создания", example = "asc")
            String direction,

            @RequestParam(required = false)
            @Parameter(description = "Статус заказа")
            OrderStatus status,

            @RequestParam(required = false)
            @Parameter(description = "Дата создания заказа", example = "2026-10-14")
            String createdAt
    ) {
        Page<OrderResponseDto> result = service.getAll(page, size, direction, status, createdAt);
        return ResponseEntity.ok(result);
    }
}