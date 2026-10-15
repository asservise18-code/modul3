package com.example.modul3.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EmailException.class,
            EmployeeIdException.class,
            PasswordException.class,
            ClientIdException.class,
            OrderIdException.class,
            ProductIdException.class,
            ProductPriceException.class,
            ProductIsEmptyNameException.class,
            ProductNameException.class,
            OrderItemIdException.class,
            ProductInOrdersException.class,
            InvalidQuantityException.class,
            InvalidCredentialsException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(getStatus(ex))
                .body(new ErrorResponse(
                        "Ошибка",
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ignored,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "Ошибка",
                        "Некорректное тело запроса. Проверь поле status",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "Ошибка валидации",
                        message,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "Внутренняя ошибка сервера",
                        "Произошла непредвиденная ошибка",
                        request.getRequestURI()
                ));
    }

    private HttpStatus getStatus(RuntimeException ex) {
        ResponseStatus responseStatus = ex.getClass()
                .getAnnotation(ResponseStatus.class);

        return responseStatus != null
                ? responseStatus.value()
                : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}