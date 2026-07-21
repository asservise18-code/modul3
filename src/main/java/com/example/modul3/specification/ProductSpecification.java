package com.example.modul3.specification;

import com.example.modul3.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> priceLessThanOrEqualTo(BigDecimal maxPrice) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
