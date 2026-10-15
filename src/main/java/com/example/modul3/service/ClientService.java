package com.example.modul3.service;

import com.example.modul3.dto.ClientRequestDto;
import com.example.modul3.dto.ClientResponseDto;
import com.example.modul3.exception.ClientIdException;
import com.example.modul3.exception.EmailException;
import com.example.modul3.mapper.ClientMapper;
import com.example.modul3.model.Client;
import com.example.modul3.model.Order;
import com.example.modul3.repository.ClientRepository;
import com.example.modul3.repository.OrderRepository;
import com.example.modul3.specification.ClientSpecification;
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


@Service
@RequiredArgsConstructor
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final OrderRepository orderRepository;

    @Transactional
    public ClientResponseDto create(ClientRequestDto dto) {
        log.info("Создание клиента с email={}", dto.getEmail());

        validateCreateRequest(dto);

        Client client = mapper.toEntity(dto);
        Client saved = repository.save(client);

        log.info("Клиент успешно создан, id={}, email={}", saved.getId(), saved.getEmail());
        return mapper.toListDto(saved);
    }

    private void validateCreateRequest(ClientRequestDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            log.warn("Попытка создать клиента с существующим email={}", dto.getEmail());
            throw new EmailException(dto.getEmail());
        }
    }

    public ClientResponseDto update(Long id, ClientRequestDto dto) {
        log.info("Обновление клиента с id={}", id);

        Client existingClient = repository.findById(id)
                .orElseThrow(() -> new ClientIdException(id));

        validateUpdateRequest(existingClient, dto);

        existingClient.setFirstName(dto.getFirstName());
        existingClient.setLastName(dto.getLastName());
        existingClient.setEmail(dto.getEmail());
        existingClient.setPhone(dto.getPhone());

        Client updated = repository.save(existingClient);

        log.info("Клиент успешно обновлён, id={}, email={}", updated.getId(), updated.getEmail());
        return mapper.toListDto(updated);
    }

    private void validateUpdateRequest(Client existingClient, ClientRequestDto dto) {
        String newEmail = dto.getEmail();

        if (!existingClient.getEmail().equals(newEmail) && repository.existsByEmail(newEmail)) {
            log.warn("Попытка обновить клиента с id={} на существующий email={}",
                    existingClient.getId(), newEmail);
            throw new EmailException(newEmail);
        }
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление клиента с id={}", id);

        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientIdException(id));

        for (Order order : client.getOrders()) {
            order.setClient(null);
            orderRepository.save(order);
        }

        repository.delete(client);

        log.info("Клиент успешно удалён, id={}", id);
    }

    @Transactional(readOnly = true)
    public ClientResponseDto getById(Long id) {
        log.info("Получение клиента по id={}", id);

        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientIdException(id));

        log.info("Клиент найден, id={}, email={}", client.getId(), client.getEmail());
        return mapper.toDto(client);
    }

    @Transactional(readOnly = true)
    public Page<ClientResponseDto> getAll(
            int page,
            int size,
            String direction,
            String firstName,
            String lastName,
            String email,
            String phone
    ) {
        log.info("Получение клиентов: страница={}, размер={}, направление={}, имя={}, фамилия={}, email={}, телефон={}",
                page, size, direction, firstName, lastName, email, phone);

        Sort sort = (direction != null && direction.equalsIgnoreCase("desc")) ?
                Sort.by("firstName").descending() :
                Sort.by("firstName").ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Client> spec = Specification.unrestricted();

        if (firstName != null && !firstName.isBlank()) {
            spec = spec.and(ClientSpecification.firstNameContains(firstName));
        }

        if (lastName != null && !lastName.isBlank()) {
            spec = spec.and(ClientSpecification.lastNameContains(lastName));
        }

        if (email != null && !email.isBlank()) {
            spec = spec.and(ClientSpecification.emailContains(email));
        }

        if (phone != null && !phone.isBlank()) {
            spec = spec.and(ClientSpecification.phoneContains(phone));
        }

        Page<ClientResponseDto> result = repository.findAll(spec, pageable)
                .map(mapper::toListDto);

        log.info("Найдено {} клиентов на странице {}", result.getNumberOfElements(), page);
        return result;
    }
}