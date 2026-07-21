package com.example.modul3.specification;

import com.example.modul3.model.Order;
import com.example.modul3.model.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {
    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<Order> createdAtBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) ->
                cb.between(root.get("createdAt"), start, end);
    }
}
