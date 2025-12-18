package com.ubs.dnc.education.academic_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlunoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(AlunoNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpServletResponse.SC_NOT_FOUND, "ID inválido", ex, request);
    }

    @ExceptionHandler(CepNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleCepNaoEncontrado(CepNaoEncontradoException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpServletResponse.SC_NOT_FOUND, "CEP não encontrado", ex, request);
    }

    @ExceptionHandler(CepInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleCepInvalido(CepInvalidoException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "CEP invalido", ex, request);
    }

    @ExceptionHandler(CepErroConsultaException.class)
    public ResponseEntity<Map<String, Object>> handleCepErroConsulta(CepErroConsultaException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpServletResponse.SC_BAD_GATEWAY, "CEP Service Error", ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error", ex, request);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(int status, String error, Exception ex, HttpServletRequest request) {
        return buildErrorResponse(status, error, ex.getMessage(), request.getServletPath(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        return buildErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "Validation error", "Validation failed for one or more fields", request.getServletPath(), fieldErrors);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(int status, String error, String message, String path, Map<String, String> fieldErrors) {
        log.warn("Exception[{}]: {}: {} at {}", status, error, message, path);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            body.put("errors", fieldErrors);
        }
        return ResponseEntity.status(status).body(body);
    }
}
