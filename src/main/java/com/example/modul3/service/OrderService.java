package com.example.modul3.service;

import com.example.modul3.dto.OrderRequestDto;
import com.example.modul3.dto.OrderResponseDto;
import com.example.modul3.exception.ClientIdException;
import com.example.modul3.exception.OrderIdException;
import com.example.modul3.mapper.OrderMapper;
import com.example.modul3.model.Client;
import com.example.modul3.model.Order;
import com.example.modul3.model.OrderStatus;
import com.example.modul3.repository.ClientRepository;
import com.example.modul3.repository.OrderRepository;
import com.example.modul3.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ClientRepository clientRepository;

    public OrderResponseDto create(OrderRequestDto dto) {
        log.info("Создание заказа для клиента с id={}", dto.getClientId());

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ClientIdException(dto.getClientId()));

        Order order = mapper.toEntity(dto);
        order.setClient(client);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = repository.save(order);

        log.info("Заказ успешно создан, id={}, id клиента={}, статус={}, дата создания={}",
                saved.getId(), client.getId(), saved.getStatus(), saved.getCreatedAt());

        return mapper.toDto(saved);
    }

    public OrderResponseDto update(Long id, OrderRequestDto dto) {
        log.info("Обновление заказа с id={}, новый id клиента={}, новый статус={}",
                id, dto.getClientId(), dto.getStatus());

        Order existingOrder = repository.findById(id)
                .orElseThrow(() -> new OrderIdException(id));

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ClientIdException(dto.getClientId()));

        log.info("Старый статус заказа={}, новый статус заказа={}",
                existingOrder.getStatus(), dto.getStatus());

        existingOrder.setStatus(dto.getStatus());
        existingOrder.setClient(client);

        Order updatedOrder = repository.save(existingOrder);

        log.info("Заказ успешно обновлён, id={}, id клиента={}, статус={}",
                updatedOrder.getId(), client.getId(), updatedOrder.getStatus());

        return mapper.toDto(updatedOrder);
    }

    public void delete(Long id) {
        log.info("Удаление заказа с id={}", id);

        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderIdException(id));

        repository.delete(order);

        log.info("Заказ успешно удалён, id={}", id);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getById(Long id) {
        log.info("Получение заказа по id={}", id);

        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderIdException(id));

        log.info("Заказ найден, id={}, id клиента={}, статус={}",
                order.getId(),
                order.getClient() != null ? order.getClient().getId() : null,
                order.getStatus());

        return mapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAll(
            int page,
            int size,
            String direction,
            OrderStatus status,
            String createdAt
    ) {
        log.info("Получение заказов: страница={}, размер={}, направление={}, статус={}, дата создания={}",
                page, size, direction, status, createdAt);

        Sort sort = (direction != null && direction.equalsIgnoreCase("asc"))
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Order> spec = Specification.unrestricted();

        if (status != null) {
            spec = spec.and(OrderSpecification.hasStatus(status));
        }

        if (createdAt != null && !createdAt.isBlank()) {
            LocalDate date = LocalDate.parse(createdAt);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);

            spec = spec.and(OrderSpecification.createdAtBetween(start, end));
        }

        Page<OrderResponseDto> result = repository.findAll(spec, pageable)
                .map(mapper::toDto);

        log.info("Найдено {} заказов на странице {}", result.getNumberOfElements(), page);
        return result;
    }
}