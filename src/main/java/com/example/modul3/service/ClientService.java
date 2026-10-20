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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final OrderRepository orderRepository;

    @Transactional
    public ClientResponseDto create(ClientRequestDto dto) {
        validateCreateRequest(dto);

        Client client = mapper.toEntity(dto);
        Client saved = repository.save(client);

        return mapper.toListDto(saved);
    }

    private void validateCreateRequest(ClientRequestDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            log.warn("Попытка создать клиента с существующим email={}", dto.getEmail());
            throw new EmailException(dto.getEmail());
        }
    }

    public ClientResponseDto update(Long id, ClientRequestDto dto) {
        Client existingClient = repository.findById(id)
                .orElseThrow(() -> new ClientIdException(id));

        validateUpdateRequest(existingClient, dto);

        existingClient.setFirstName(dto.getFirstName());
        existingClient.setLastName(dto.getLastName());
        existingClient.setEmail(dto.getEmail());
        existingClient.setPhone(dto.getPhone());

        Client updated = repository.save(existingClient);

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
        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientIdException(id));

        for (Order order : client.getOrders()) {
            order.setClient(null);
            orderRepository.save(order);
        }

        repository.delete(client);
    }

    @Transactional(readOnly = true)
    public ClientResponseDto getById(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientIdException(id));

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

        return repository.findAll(spec, pageable)
                .map(mapper::toListDto);
    }
}