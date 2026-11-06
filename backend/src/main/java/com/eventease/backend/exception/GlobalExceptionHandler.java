package com.eventease.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(IllegalArgumentException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleConflict(IllegalStateException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(new ValidationErrorResponse("Validation failed", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message) {
        return new ResponseEntity<>(new ErrorResponse(status.value(), message), status);
    }

    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse() {}
        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class ValidationErrorResponse {
        private int status;
        private String message;
        private Map<String, String> errors;

        public ValidationErrorResponse(String message, Map<String, String> errors) {
            this.status = HttpStatus.BAD_REQUEST.value();
            this.message = message;
            this.errors = errors;
        }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Map<String, String> getErrors() { return errors; }
        public void setErrors(Map<String, String> errors) { this.errors = errors; }
    }
}
