package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductInOrdersException extends RuntimeException {
    public ProductInOrdersException(Long productId) {
        super("Товар с id = " + productId + " связан с заказами и не может быть удалён");
    }
}