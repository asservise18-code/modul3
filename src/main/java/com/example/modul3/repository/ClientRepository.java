package com.example.modul3.repository;

import com.example.modul3.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends JpaRepository<Client, Long>,
        JpaSpecificationExecutor<Client> {

    boolean existsByEmail(String email);
}
