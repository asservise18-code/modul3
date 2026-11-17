package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailException extends RuntimeException {
    public EmailException(String email) {
        super("Email '" + email + "' уже используется");
    }
}