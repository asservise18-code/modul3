package com.example.modul3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EmailException.class,
            EmployeeIdException.class,
            PasswordException.class,
            ClientIdException.class,
            OrderIdException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomException(RuntimeException ex) {
        return ResponseEntity.status(getStatus(ex))
                .body(new ErrorResponse("Ошибка", ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ignored) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Ошибка", "Некорректное тело запроса. Проверь поле status"));
    }

    private HttpStatus getStatus(RuntimeException ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        return responseStatus != null
                ? responseStatus.value()
                : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}