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
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ClientRepository clientRepository;

    public OrderResponseDto create(OrderRequestDto dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ClientIdException(dto.getClientId()));

        Order order = mapper.toEntity(dto);
        order.setClient(client);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = repository.save(order);
        return mapper.toDto(saved);

    }

    public OrderResponseDto update(Long id, OrderRequestDto dto) {
        Order existingOrder = repository.findById(id)
                .orElseThrow(() -> new OrderIdException(id));

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ClientIdException(dto.getClientId()));

        existingOrder.setStatus(dto.getStatus());
        existingOrder.setClient(client);

        Order updatedOrder = repository.save(existingOrder);
        return mapper.toDto(updatedOrder);

    }

    public void delete(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderIdException(id));
        repository.delete(order);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderIdException(id));
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

        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

}
