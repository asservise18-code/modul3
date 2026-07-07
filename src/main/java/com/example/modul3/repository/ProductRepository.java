package com.example.modul3.repository;

import com.example.modul3.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;


public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    Page<Product> findByNameContainingIgnoreCaseAndPriceBetween(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
}