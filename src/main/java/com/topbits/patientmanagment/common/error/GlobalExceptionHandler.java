package com.topbits.patientmanagment.common.error;

import tools.jackson.databind.exc.InvalidFormatException;
import com.topbits.patientmanagment.common.exception.ConflictException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import com.topbits.patientmanagment.common.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
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
        Throwable most = ex.getMostSpecificCause();
        if (most instanceof InvalidFormatException ifx
                && ifx.getTargetType() != null
                && ifx.getTargetType().isEnum()) {

            String fieldName = (ifx.getPath() != null && !ifx.getPath().isEmpty())
                    ? ifx.getPath().getLast().getPropertyName()
                    : "unknown";

            String errorDetails = String.format(
                    "Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                    ifx.getValue(),
                    fieldName,
                    Arrays.toString(ifx.getTargetType().getEnumConstants())
            );

            ApiError err = ApiError.builder()
                    .code("VALIDATION_ERROR")
                    .message("Invalid request body")
                    .details(List.of(new ApiError.FieldErrorItem(fieldName, errorDetails)))
                    .path(req.getRequestURI())
                    .timestamp(Instant.now())
                    .build();

            return ResponseEntity.badRequest().body(err);
        }

        ApiError err = ApiError.builder()
                .code("VALIDATION_ERROR")
                .message("Invalid request body")
                .details(List.of())
                .path(req.getRequestURI())
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.badRequest().body(err);
    }



    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {

        String field = ex.getName(); // "status"
        String allowed = "";

        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            Object[] enums = ex.getRequiredType().getEnumConstants();
            allowed = java.util.Arrays.stream(enums)
                    .map(Object::toString)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        }

        ApiError err = ApiError.builder()
                .code("VALIDATION_ERROR")
                .message("Invalid query parameter")
                .details(java.util.List.of(
                        new ApiError.FieldErrorItem(field,
                                "Invalid value '" + ex.getValue() + "'" +
                                        (allowed.isBlank() ? "" : ". Allowed values: " + allowed)
                        )
                ))
                .path(req.getRequestURI())
                .timestamp(java.time.Instant.now())
                .build();

        return ResponseEntity.badRequest().body(err);
    }

}
