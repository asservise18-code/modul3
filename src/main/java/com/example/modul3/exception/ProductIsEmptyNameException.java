package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductIsEmptyNameException extends RuntimeException {
    public ProductIsEmptyNameException() {
        super("Название продукта не должно быть пустым");
    }
}