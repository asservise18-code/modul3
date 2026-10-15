package com.example.modul3.controller;

import com.example.modul3.dto.EmployeeRequestDto;
import com.example.modul3.dto.EmployeeResponseDto;
import com.example.modul3.dto.LoginRequestDto;
import com.example.modul3.dto.LoginResponseDto;
import com.example.modul3.model.Role;
import com.example.modul3.service.EmployeeService;
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
        name = "Employee Controller",
        description = "Контроллер для управления сотрудниками и авторизации"
)
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @Operation(
            summary = "Создание сотрудника",
            description = "Позволяет создать нового сотрудника"
    )
    @ApiResponse(responseCode = "201", description = "Сотрудник создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    @ApiResponse(responseCode = "409", description = "Сотрудник с таким email уже существует")
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> create(
            @Valid @RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto result = service.create(dto);
        return ResponseEntity.status(201).body(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Обновление сотрудника",
            description = "Позволяет обновить данные сотрудника по его идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Сотрудник обновлён")
    @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    @ApiResponse(responseCode = "409", description = "Сотрудник с таким email уже существует")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> update(
            @PathVariable
            @Parameter(description = "Идентификатор сотрудника", required = true)
            Long id,

            @Valid @RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto result = service.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение сотрудника по id",
            description = "Позволяет получить данные сотрудника по его идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Сотрудник найден")
    @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getById(
            @PathVariable
            @Parameter(description = "Идентификатор сотрудника", required = true)
            Long id) {
        EmployeeResponseDto result = service.getById(id);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Получение списка сотрудников",
            description = "Позволяет получить сотрудников с пагинацией, сортировкой и фильтрацией"
    )
    @ApiResponse(responseCode = "200", description = "Список сотрудников получен")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> getAll(
            @RequestParam(defaultValue = "0")
            @Parameter(description = "Номер страницы", example = "0")
            int page,

            @RequestParam(defaultValue = "10")
            @Parameter(description = "Количество сотрудников на странице", example = "10")
            int size,

            @RequestParam(defaultValue = "id")
            @Parameter(description = "Поле для сортировки", example = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            @Parameter(description = "Направление сортировки", example = "asc")
            String direction,

            @RequestParam(required = false)
            @Parameter(description = "Роль сотрудника")
            Role role,

            @RequestParam(required = false)
            @Parameter(description = "Имя сотрудника", example = "Ivan")
            String firstName) {
        Page<EmployeeResponseDto> result = service.getAll(page, size, sortBy, direction, role, firstName);
        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удаление сотрудника",
            description = "Позволяет удалить сотрудника по его идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Сотрудник удалён")
    @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Parameter(description = "Идентификатор сотрудника", required = true)
            Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Авторизация сотрудника",
            description = "Позволяет получить JWT-токен для доступа к защищённым endpoint'ам"
    )
    @ApiResponse(responseCode = "200", description = "Успешная авторизация")
    @ApiResponse(responseCode = "401", description = "Неверный email или пароль")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto) {
        String token = service.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}