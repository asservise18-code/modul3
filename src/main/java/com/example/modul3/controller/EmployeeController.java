package com.example.modul3.controller;

import com.example.modul3.dto.EmployeeRequestDto;
import com.example.modul3.dto.EmployeeResponseDto;
import com.example.modul3.model.Role;
import com.example.modul3.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> create(@RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto result = service.create(dto);
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> update(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDto dto) {
        EmployeeResponseDto result = service.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getById(@PathVariable Long id) {
        EmployeeResponseDto result = service.getById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String firstName) {
        Page<EmployeeResponseDto> result = service.getAll(page, size, sortBy, direction, role, firstName);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}