package com.example.modul3.exception;

public class ProductInOrdersException extends RuntimeException {
    public ProductInOrdersException(Long productId) {
        super("Товар с id = " + productId + " связан с заказами и не может быть удалён");
    }
}