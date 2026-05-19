package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderIdException extends RuntimeException {
    public OrderIdException(Long id) {
        super("Заказ с ID " + id + " не найден");
    }
}


