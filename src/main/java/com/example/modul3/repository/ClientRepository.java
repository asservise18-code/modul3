package com.example.modul3.repository;

import com.example.modul3.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByEmail(String email);

    Page<Client> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    Page<Client> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

    Page<Client> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Client> findByPhoneContainingIgnoreCase(String phone, Pageable pageable);
}
