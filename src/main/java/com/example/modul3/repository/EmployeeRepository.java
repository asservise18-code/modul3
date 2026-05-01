package com.example.modul3.repository;

import com.example.modul3.model.Employee;
import com.example.modul3.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    Page<Employee> findByRole(Role role, Pageable pageable);

    Page<Employee> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    Page<Employee> findByRoleAndFirstNameContainingIgnoreCase(Role role, String firstName, Pageable pageable);


}