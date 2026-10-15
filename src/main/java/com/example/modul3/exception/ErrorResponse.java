package com.example.modul3.exception;

import java.time.Instant;

public record ErrorResponse(
        String error,
        String message,
        String path,
        Instant timestamp
) {
    public ErrorResponse(String error, String message, String path) {
        this(error, message, path, Instant.now());
    }

    public ErrorResponse(String error, String message) {
        this(error, message, null, Instant.now());
    }
}