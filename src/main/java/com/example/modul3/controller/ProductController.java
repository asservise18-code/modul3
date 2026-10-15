package com.example.modul3.controller;

import com.example.modul3.dto.ProductRequestDto;
import com.example.modul3.dto.ProductResponseDto;
import com.example.modul3.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Product Controller",
        description = "Контроллер для управления товарами"
)
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Operation(
            summary = "Создание товара",
            description = "Позволяет создать новый товар"
    )
    @ApiResponse(responseCode = "201", description = "Товар создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные товара")
    @ApiResponse(responseCode = "409", description = "Товар с таким названием уже существует")
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
            @Valid @RequestBody ProductRequestDto dto) {
        ProductResponseDto result = service.create(dto);
        return ResponseEntity.status(201).body(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Обновление товара",
            description = "Позволяет обновить данные товара по его идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Товар обновлён")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    @ApiResponse(responseCode = "409", description = "Товар с таким названием уже существует")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable
            @Parameter(description = "Идентификатор товара", required = true)
            Long id,

            @Valid @RequestBody ProductRequestDto dto) {
        ProductResponseDto result = service.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение списка товаров",
            description = "Позволяет получить товары с пагинацией, сортировкой и фильтрацией по названию и цене"
    )
    @ApiResponse(responseCode = "200", description = "Список товаров получен")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAll(
            @RequestParam(defaultValue = "0")
            @Parameter(description = "Номер страницы", example = "0")
            int page,

            @RequestParam(defaultValue = "10")
            @Parameter(description = "Количество товаров на странице", example = "10")
            int size,

            @RequestParam(defaultValue = "name")
            @Parameter(description = "Поле для сортировки", example = "name")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            @Parameter(description = "Направление сортировки", example = "asc")
            String direction,

            @RequestParam(required = false)
            @Parameter(description = "Название товара", example = "Laptop")
            String name,

            @RequestParam(required = false)
            @Parameter(description = "Минимальная цена товара", example = "100.00")
            BigDecimal minPrice,

            @RequestParam(required = false)
            @Parameter(description = "Максимальная цена товара", example = "5000.00")
            BigDecimal maxPrice
    ) {
        return ResponseEntity.ok(
                service.getAll(page, size, sortBy, direction, name, minPrice, maxPrice)
        );
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение товара по id",
            description = "Позволяет получить данные товара по его идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Товар найден")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(
            @PathVariable
            @Parameter(description = "Идентификатор товара", required = true)
            Long id) {
        ProductResponseDto result = service.getById(id);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление товара",
            description = "Позволяет удалить товар по его идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Товар удалён")
    @ApiResponse(responseCode = "404", description = "Товар не найден")
    @ApiResponse(responseCode = "409", description = "Товар используется в заказах и не может быть удалён")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Parameter(description = "Идентификатор товара", required = true)
            Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}