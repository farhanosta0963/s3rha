package com.s3rha.spring.controller;

import com.s3rha.spring.dto.Violation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===== 4xx Client Errors =====
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<Violation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new Violation(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        log.warn("Validation failed for {} - {} error(s): [{}] | Request: {}",
                ex.getParameter().getMethod().getName(),
                violations.size(),
                violations.stream()
                        .map(v -> v.getField() + ": " + v.getMessage())

                        .collect(Collectors.joining(", ")),
                request.getDescription(false));

        return new ErrorResponse("Validation failed", violations);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<Violation> violations = ex.getConstraintViolations()
                .stream()
                .map(violation -> new Violation(
                        getFieldName(violation.getPropertyPath().toString()),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());

        log.warn("Constraint violation for {} - {} error(s): [{}] | Request: {}",
                ex.getClass().getSimpleName(),
                violations.size(),
                violations.stream()
                        .map(v -> v.getField() + ": " + v.getMessage())
                        .collect(Collectors.joining(", ")),
                request.getDescription(false));

        return new ErrorResponse("Constraint violation", violations);
    }

    // ===== Custom Business Exceptions =====
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.warn("Resource not found: {} | Request: {}",
                ex.getMessage(),
                request.getDescription(false));
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        log.warn("Access denied for request: {} | Reason: {}",
                request.getDescription(false),
                ex.getMessage());
        return new ErrorResponse("Access denied");
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResponse handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        log.warn("Response status exception [{}]: {} | Request: {}",
                ex.getStatusCode(),
                ex.getReason(),
                request.getDescription(false));
        return new ErrorResponse(ex.getReason());
    }

    // ===== 5xx Server Errors =====
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Unexpected error during {} | Error: {} | Trace: ",
                request.getDescription(false),
                ex.getMessage(),
                ex);
        return new ErrorResponse("An unexpected error occurred");
    }

    private String getFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts.length > 0 ? parts[parts.length - 1] : propertyPath;
    }
}