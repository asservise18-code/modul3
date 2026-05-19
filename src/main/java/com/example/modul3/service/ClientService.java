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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
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
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by("firstName").descending() :
                Sort.by("firstName").ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Client> clientPage;

        if (firstName != null && !firstName.isBlank()) {
            clientPage = repository.findByFirstNameContainingIgnoreCase(firstName, pageable);
        } else if (lastName != null && !lastName.isBlank()) {
            clientPage = repository.findByLastNameContainingIgnoreCase(lastName, pageable);
        } else if (email != null && !email.isBlank()) {
            clientPage = repository.findByEmailContainingIgnoreCase(email, pageable);
        } else if (phone != null && !phone.isBlank()) {
            clientPage = repository.findByPhoneContainingIgnoreCase(phone, pageable);
        } else clientPage = repository.findAll(pageable);

        return clientPage.map(mapper::toListDto);
    }

}
