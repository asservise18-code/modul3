package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordException extends RuntimeException {
    public PasswordException() {
        super("Пароль должен содержать минимум 6 символов");
    }
}
