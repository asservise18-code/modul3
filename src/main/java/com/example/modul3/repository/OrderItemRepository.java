package com.example.modul3.repository;

import com.example.modul3.model.Order;
import com.example.modul3.model.OrderItem;
import com.example.modul3.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);

    List<OrderItem> findByOrderId(Long orderId);

    boolean existsByProductId(Long productId);
}
