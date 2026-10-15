package com.example.modul3.service;

import com.example.modul3.dto.EmployeeRequestDto;
import com.example.modul3.dto.EmployeeResponseDto;
import com.example.modul3.exception.EmailException;
import com.example.modul3.exception.EmployeeIdException;
import com.example.modul3.exception.InvalidCredentialsException;
import com.example.modul3.mapper.EmployeeMapper;
import com.example.modul3.model.Employee;
import com.example.modul3.model.Role;
import com.example.modul3.repository.EmployeeRepository;
import com.example.modul3.security.JwtService;
import com.example.modul3.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public EmployeeResponseDto create(EmployeeRequestDto dto) {
        log.info("Создание сотрудника с email={}", dto.getEmail());

        validateCreateRequest(dto);

        Employee employee = mapper.toEntity(dto);
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));

        Employee saved = repository.save(employee);

        log.info("Сотрудник успешно создан, id={}, email={}", saved.getId(), saved.getEmail());
        return mapper.toResponseDto(saved);
    }

    private void validateCreateRequest(EmployeeRequestDto dto) {
        if (repository.findByEmail(dto.getEmail()).isPresent()) {
            log.warn("Попытка создать сотрудника с существующим email={}", dto.getEmail());
            throw new EmailException(dto.getEmail());
        }
    }

    public EmployeeResponseDto update(Long id, EmployeeRequestDto dto) {
        log.info("Обновление сотрудника с id={}", id);

        Employee existingEmployee = repository.findById(id)
                .orElseThrow(() -> new EmployeeIdException(id));

        validateUpdateRequest(id, dto);

        Employee employee = mapper.toEntity(dto);
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setId(existingEmployee.getId());

        Employee updated = repository.save(employee);

        log.info("Сотрудник успешно обновлён, id={}, email={}", updated.getId(), updated.getEmail());
        return mapper.toResponseDto(updated);
    }

    private void validateUpdateRequest(Long id, EmployeeRequestDto dto) {
        repository.findByEmail(dto.getEmail())
                .filter(emp -> !emp.getId().equals(id))
                .ifPresent(emp -> {
                    log.warn("Попытка обновить сотрудника с id={} на существующий email={}", id, dto.getEmail());
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
        log.info("Получение сотрудников: страница={}, размер={}, сортировка={}, направление={}, роль={}, имя={}",
                page, size, sortBy, direction, role, firstName);

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

        Page<EmployeeResponseDto> result = repository.findAll(spec, pageable)
                .map(mapper::toResponseDto);

        log.info("Найдено {} сотрудников на странице {}", result.getNumberOfElements(), page);
        return result;
    }

    public EmployeeResponseDto getById(Long id) {
        log.info("Получение сотрудника по id={}", id);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeIdException(id));

        log.info("Сотрудник найден, id={}, email={}", employee.getId(), employee.getEmail());
        return mapper.toResponseDto(employee);
    }

    public void delete(Long id) {
        log.info("Удаление сотрудника с id={}", id);

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeIdException(id));

        repository.deleteById(employee.getId());

        log.info("Сотрудник успешно удалён, id={}", id);
    }

    public String login(String email, String rawPassword) {
        log.info("Попытка входа для email={}", email);

        Employee employee = repository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(rawPassword, employee.getPassword())) {
            log.warn("Неудачная попытка входа для email={}", email);
            throw new InvalidCredentialsException();
        }

        log.info("Сотрудник успешно вошёл в систему, email={}", email);
        return jwtService.generateToken(employee.getEmail());
    }
}