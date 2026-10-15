package com.example.modul3.controller;

import com.example.modul3.dto.ClientRequestDto;
import com.example.modul3.dto.ClientResponseDto;
import com.example.modul3.service.ClientService;
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
        name = "Client Controller",
        description = "Контроллер для управления клиентами"
)
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @Operation(
            summary = "Создание клиента",
            description = "Позволяет создать нового клиента"
    )
    @ApiResponse(responseCode = "201", description = "Клиент создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    @ApiResponse(responseCode = "409", description = "Клиент с таким email уже существует")
    @PostMapping
    public ResponseEntity<ClientResponseDto> create(
            @Valid @RequestBody ClientRequestDto dto) {
        ClientResponseDto result = service.create(dto);
        return ResponseEntity.status(201).body(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Обновление клиента",
            description = "Позволяет обновить данные клиента по его идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Клиент обновлён")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @ApiResponse(responseCode = "409", description = "Клиент с таким email уже существует")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDto> update(
            @PathVariable
            @Parameter(description = "Идентификатор клиента", required = true)
            Long id,

            @Valid @RequestBody ClientRequestDto dto) {
        ClientResponseDto result = service.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление клиента",
            description = "Позволяет удалить клиента по его идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Клиент удалён")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Parameter(description = "Идентификатор клиента", required = true)
            Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение клиента по id",
            description = "Позволяет получить данные клиента по его идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Клиент найден")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getById(
            @PathVariable
            @Parameter(description = "Идентификатор клиента", required = true)
            Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение списка клиентов",
            description = "Позволяет получить клиентов с пагинацией, сортировкой и фильтрацией"
    )
    @ApiResponse(responseCode = "200", description = "Список клиентов получен")
    @GetMapping
    public ResponseEntity<Page<ClientResponseDto>> getAll(
            @RequestParam(defaultValue = "0")
            @Parameter(description = "Номер страницы", example = "0")
            int page,

            @RequestParam(defaultValue = "10")
            @Parameter(description = "Количество клиентов на странице", example = "10")
            int size,

            @RequestParam(defaultValue = "asc")
            @Parameter(description = "Направление сортировки", example = "asc")
            String direction,

            @RequestParam(required = false)
            @Parameter(description = "Имя клиента", example = "Anna")
            String firstName,

            @RequestParam(required = false)
            @Parameter(description = "Фамилия клиента", example = "Petrova")
            String lastName,

            @RequestParam(required = false)
            @Parameter(description = "Email клиента", example = "anna@example.com")
            String email,

            @RequestParam(required = false)
            @Parameter(description = "Телефон клиента", example = "+994501234567")
            String phone
    ) {
        Page<ClientResponseDto> result = service.getAll(page, size, direction, firstName, lastName, email, phone);
        return ResponseEntity.ok(result);
    }
}