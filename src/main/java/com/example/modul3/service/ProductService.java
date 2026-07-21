package com.example.modul3.service;

import com.example.modul3.dto.ProductRequestDto;
import com.example.modul3.dto.ProductResponseDto;
import com.example.modul3.exception.*;
import com.example.modul3.mapper.ProductMapper;
import com.example.modul3.model.Product;
import com.example.modul3.repository.OrderItemRepository;
import com.example.modul3.repository.ProductRepository;
import com.example.modul3.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final OrderItemRepository orderItemRepository;

    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        validateProduct(productRequestDto);

        if (repository.existsByName(productRequestDto.getName())) {
            throw new ProductNameException(productRequestDto.getName());
        }

        Product product = mapper.toEntity(productRequestDto);
        Product savedProduct = repository.save(product);

        return mapper.toResponseDto(savedProduct);

    }

    private void validateProduct(ProductRequestDto productRequestDto) {
        if (productRequestDto.getName() == null || productRequestDto.getName().trim().isEmpty()) {
            throw new ProductIsEmptyNameException();
        }

        if (productRequestDto.getPrice() == null || productRequestDto.getPrice().signum() < 0) {
            throw new ProductPriceException();
        }
    }


    public Page<ProductResponseDto> getAll(
            int page,
            int size,
            String sortBy,
            String direction,
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        Sort sort = (direction != null && direction.equalsIgnoreCase("desc"))
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Product> spec = Specification.unrestricted();

        if (name != null && !name.isBlank()) {
            spec = spec.and(ProductSpecification.nameContains(name));
        }

        if (minPrice != null) {
            spec = spec.and(ProductSpecification.priceGreaterThanOrEqualTo(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.priceLessThanOrEqualTo(maxPrice));
        }

        return repository.findAll(spec, pageable)
                .map(mapper::toResponseDto);
    }

    public ProductResponseDto getById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductIdException(id));

        return mapper.toResponseDto(product);
    }

    public ProductResponseDto update(Long id, ProductRequestDto productRequestDto) {
        validateProduct(productRequestDto);

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductIdException(id));

        if (repository.existsByName(productRequestDto.getName())
                && !product.getName().equals(productRequestDto.getName())) {
            throw new ProductNameException(productRequestDto.getName());
        }

        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());

        Product updatedProduct = repository.save(product);

        return mapper.toResponseDto(updatedProduct);
    }


    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductIdException(id));
        if (orderItemRepository.existsByProductId(id)) {
            throw new ProductInOrdersException(id);
        }
        repository.delete(product);
    }

}
