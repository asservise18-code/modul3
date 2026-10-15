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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final OrderItemRepository orderItemRepository;

    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        log.info("Создание товара с названием={}, ценой={}",
                productRequestDto.getName(), productRequestDto.getPrice());

        validateProduct(productRequestDto);

        if (repository.existsByName(productRequestDto.getName())) {
            log.warn("Попытка создать товар с существующим названием={}", productRequestDto.getName());
            throw new ProductNameException(productRequestDto.getName());
        }

        Product product = mapper.toEntity(productRequestDto);
        Product savedProduct = repository.save(product);

        log.info("Товар успешно создан, id={}, название={}, цена={}",
                savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice());

        return mapper.toResponseDto(savedProduct);
    }

    private void validateProduct(ProductRequestDto productRequestDto) {
        if (productRequestDto.getName() == null || productRequestDto.getName().trim().isEmpty()) {
            log.warn("Название товара пустое или отсутствует");
            throw new ProductIsEmptyNameException();
        }

        if (productRequestDto.getPrice() == null || productRequestDto.getPrice().signum() < 0) {
            log.warn("Некорректная цена товара={}", productRequestDto.getPrice());
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
        log.info("Получение товаров: страница={}, размер={}, сортировка={}, направление={}, название={}, минимальная цена={}, максимальная цена={}",
                page, size, sortBy, direction, name, minPrice, maxPrice);

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

        Page<ProductResponseDto> result = repository.findAll(spec, pageable)
                .map(mapper::toResponseDto);

        log.info("Найдено {} товаров на странице {}", result.getNumberOfElements(), page);
        return result;
    }

    public ProductResponseDto getById(Long id) {
        log.info("Получение товара по id={}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductIdException(id));

        log.info("Товар найден, id={}, название={}, цена={}",
                product.getId(), product.getName(), product.getPrice());

        return mapper.toResponseDto(product);
    }

    public ProductResponseDto update(Long id, ProductRequestDto productRequestDto) {
        log.info("Обновление товара с id={}, новое название={}, новая цена={}",
                id, productRequestDto.getName(), productRequestDto.getPrice());

        validateProduct(productRequestDto);

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductIdException(id));

        if (repository.existsByName(productRequestDto.getName())
                && !product.getName().equals(productRequestDto.getName())) {
            log.warn("Попытка обновить товар с id={} на существующее название={}",
                    id, productRequestDto.getName());
            throw new ProductNameException(productRequestDto.getName());
        }

        log.info("Старое название товара={}, старая цена={}",
                product.getName(), product.getPrice());

        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());

        Product updatedProduct = repository.save(product);

        log.info("Товар успешно обновлён, id={}, название={}, цена={}",
                updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getPrice());

        return mapper.toResponseDto(updatedProduct);
    }


    public void delete(Long id) {
        log.info("Удаление товара с id={}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductIdException(id));

        if (orderItemRepository.existsByProductId(id)) {
            log.warn("Попытка удалить товар с id={}, который используется в заказах", id);
            throw new ProductInOrdersException(id);
        }

        repository.delete(product);

        log.info("Товар успешно удалён, id={}", id);
    }
}