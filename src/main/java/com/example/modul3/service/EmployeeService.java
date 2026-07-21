package com.example.modul3.service;

import com.example.modul3.dto.EmployeeRequestDto;
import com.example.modul3.dto.EmployeeResponseDto;
import com.example.modul3.exception.EmailException;
import com.example.modul3.exception.EmployeeIdException;
import com.example.modul3.mapper.EmployeeMapper;
import com.example.modul3.model.Employee;
import com.example.modul3.model.Role;
import com.example.modul3.repository.EmployeeRepository;
import com.example.modul3.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;


    public EmployeeResponseDto create(EmployeeRequestDto dto) {
        validateCreateRequest(dto);
        Employee employee = mapper.toEntity(dto);
        Employee saved = repository.save(employee);
        return mapper.toResponseDto(saved);
    }

    private void validateCreateRequest(EmployeeRequestDto dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailException(dto.getEmail());
        }
    }

    public EmployeeResponseDto update(Long id, EmployeeRequestDto dto) {
        Employee existingEmployee = repository.findById(id)
                .orElseThrow(() -> new EmployeeIdException(id));

        validateUpdateRequest(id, dto);

        Employee employee = mapper.toEntity(dto);
        employee.setId(existingEmployee.getId());

        Employee updated = repository.save(employee);
        return mapper.toResponseDto(updated);
    }

    private void validateUpdateRequest(Long id, EmployeeRequestDto dto) {
        repository.findByEmail(dto.getEmail())
                .filter(emp -> !emp.getId().equals(id))
                .ifPresent(emp -> {
                    throw new EmailException(dto.getEmail());
                });
    }

    public Page<EmployeeResponseDto> getAll(
            int page,
            int size,
            String sortBy,
            String direction,
            Role role,
            String firstName
    ) {
        Sort sort = (direction != null && direction.equalsIgnoreCase("desc"))
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = Specification.unrestricted();

        if (role != null) {
            spec = spec.and(EmployeeSpecification.hasRole(role));
        }

        if (firstName != null && !firstName.isBlank()) {
            spec = spec.and(EmployeeSpecification.firstNameContains(firstName));
        }

        return repository.findAll(spec, pageable)
                .map(mapper::toResponseDto);
    }
    public EmployeeResponseDto getById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeIdException(id));
        return mapper.toResponseDto(employee);
    }

    public void delete(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeIdException(id));

        repository.deleteById(employee.getId());
    }
}

