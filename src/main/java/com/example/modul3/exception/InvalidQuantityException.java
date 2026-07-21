package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
        super("Количество должно быть больше 0");
    }
}