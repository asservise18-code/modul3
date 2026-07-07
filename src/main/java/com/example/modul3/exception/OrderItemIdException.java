package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderItemIdException extends RuntimeException {
    public OrderItemIdException(Long id) {
        super("Элемент заказа с идентификатором " + id + " не найден");
    }
}