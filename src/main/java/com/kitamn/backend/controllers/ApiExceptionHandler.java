package com.kitamn.backend.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        // ② Convert any IllegalArgumentException thrown from services/controllers
        //    into a 400 Bad Request with a small JSON body { "error": "message" }.
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidBody(MethodArgumentNotValidException ex) {
        // ③ Happens when @Valid on a @RequestBody fails (Bean Validation).
        //    We extract the first field error and show a human message.
        var first = ex.getBindingResult().getFieldErrors().stream().findFirst();
        var msg = first.map(f -> f.getField() + " " + f.getDefaultMessage())
                .orElse("Invalid request");
        return ResponseEntity.badRequest().body(Map.of("error", msg));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidParams(ConstraintViolationException ex) {
        // ④ Happens when validation fails on path/query params (e.g., @Min on @PathVariable).
        //    We return 400 with the violation message.
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
