package com.example.auth.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArg(IllegalArgumentException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", ex.getMessage());
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("error", "Validación fallida");
    List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors()
      .stream()
      .map(fe -> {
        Map<String, String> m = new HashMap<>();
        m.put("field", fe.getField());
        m.put("message", fe.getDefaultMessage());
        return m;
      })
      .collect(Collectors.toList());
    body.put("fieldErrors", fieldErrors);
    return ResponseEntity.badRequest().body(body);
  }
}
