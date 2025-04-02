package com.s3rha.spring.controller;

import java.time.Instant;
import java.util.List;

public class ErrorResponse {
    private String message;
    private Instant timestamp = Instant.now();
    private List<?> details;  // Can hold either Strings or Violation objects

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, List<?> details) {
        this.message = message;
        this.details = details;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public List<?> getDetails() {
        return details;
    }
}