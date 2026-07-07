package com.example.modul3.controller;

import com.example.modul3.dto.ProductRequestDto;
import com.example.modul3.dto.ProductResponseDto;
import com.example.modul3.mapper.ProductMapper;
import com.example.modul3.model.Product;
import com.example.modul3.repository.ProductRepository;
import com.example.modul3.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final ProductRepository repository;

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto dto) {
        ProductResponseDto result = service.create(dto);
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @RequestBody ProductRequestDto dto) {
        ProductResponseDto result = service.update(id, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        return ResponseEntity.ok(
                service.getAll(page, size, sortBy, direction, name, minPrice, maxPrice)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        ProductResponseDto result = service.getById(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
