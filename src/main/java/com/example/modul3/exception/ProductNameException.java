package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNameException extends RuntimeException {
    public ProductNameException(String name) {
        super("Продукт с названием \"" + name + "\" уже существует");
    }
}