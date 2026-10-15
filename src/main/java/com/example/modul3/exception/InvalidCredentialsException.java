package com.example.modul3.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Неверный email или пароль");
    }
}