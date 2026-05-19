package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientIdException extends RuntimeException {
    public ClientIdException(Long id) {
        super("Клиент с ID " + id + " не найден");
    }
}


