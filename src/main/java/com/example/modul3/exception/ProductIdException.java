package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductIdException extends RuntimeException {

    public ProductIdException(Long id) {
        super("Продукт с ID " + id + " не найден");
    }
}