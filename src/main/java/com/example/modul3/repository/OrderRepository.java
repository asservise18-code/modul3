package com.example.modul3.repository;

import com.example.modul3.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}