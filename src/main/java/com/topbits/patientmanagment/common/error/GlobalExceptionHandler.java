package com.topbits.patientmanagment.common.error;

import com.topbits.patientmanagment.common.exception.ConflictException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest req) {

        List<ApiError.FieldErrorItem> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ApiError.FieldErrorItem(fe.getField(), fe.getDefaultMessage()))
                .toList();

        ApiError err = ApiError.builder()
                .code("VALIDATION_ERROR")
                .message("Invalid request")
                .details(details)
                .path(req.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex, HttpServletRequest req) {

        ApiError err = ApiError.builder()
                .code("CONFLICT")
                .message(ex.getMessage())
                .details(List.of())
                .path(req.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest req) {

        ApiError err = ApiError.builder()
                .code("NOT_FOUND")
                .message(ex.getMessage())
                .details(List.of())
                .path(req.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {

        ApiError err = ApiError.builder()
                .code("VALIDATION_ERROR")
                .message("Invalid request body")
                .details(List.of())
                .path(req.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.badRequest().body(err);
    }


}
