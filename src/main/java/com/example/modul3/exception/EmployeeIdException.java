package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeIdException extends RuntimeException {
    public EmployeeIdException(Long id) {
        super("Сотрудник с ID " + id + " не найден");
    }
}
