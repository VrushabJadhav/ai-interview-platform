package com.aiinterview.auth.exception;

import com.aiinterview.common.api.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var fields = ex.getBindingResult().getFieldErrors().stream().map(e -> new ErrorResponse.FieldError(e.getField(), e.getDefaultMessage())).toList();
        return ResponseEntity.badRequest().body(new ErrorResponse(java.time.Instant.now(), 400, "Bad Request", "Validation failed", req.getRequestURI(), fields));
    }
    @ExceptionHandler({IllegalArgumentException.class, BadCredentialsException.class}) ResponseEntity<ErrorResponse> badRequest(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(400, "Bad Request", ex.getMessage(), req.getRequestURI()));
    }
}
