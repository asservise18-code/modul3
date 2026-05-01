package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler({EmailException.class, EmployeeIdException.class,
            PasswordException.class})
    public ResponseEntity<ErrorResponse> handleCustomException(RuntimeException ex) {
        return ResponseEntity.status(getStatus(ex))
                .body(new ErrorResponse("Ошибка", ex.getMessage()));
    }

    private HttpStatus getStatus(RuntimeException ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        return responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}