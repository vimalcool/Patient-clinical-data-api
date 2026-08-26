package com.vimal.clinicalsapi.patientsdata.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationException(
                        MethodArgumentNotValidException exception) {

                Map<String, String> errors = new HashMap<>();
                exception.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                Map<String, Object> body = Map.of(
                                "timestamp", Instant.now(),
                                "status", HttpStatus.BAD_REQUEST.value(),
                                "error", "Validation Failed",
                                "message", "Input validation failed",
                                "details", errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, Object>> handleIllegalArgument(
                        IllegalArgumentException exception) {

                return errorResponse(
                                HttpStatus.BAD_REQUEST,
                                exception.getMessage());
        }

        @ExceptionHandler(DataAccessException.class)
        public ResponseEntity<Map<String, Object>> handleDatabaseError(
                        DataAccessException exception) {

                return errorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "A database error occurred");
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleGeneralError(
                        Exception exception) {

                return errorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred");
        }

        private ResponseEntity<Map<String, Object>> errorResponse(
                        HttpStatus status,
                        String message) {

                Map<String, Object> body = Map.of(
                                "timestamp", Instant.now(),
                                "status", status.value(),
                                "error", status.getReasonPhrase(),
                                "message", message);

                return ResponseEntity.status(status).body(body);
        }
}