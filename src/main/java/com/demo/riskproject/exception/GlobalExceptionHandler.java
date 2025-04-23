package com.demo.riskproject.exception;

import com.demo.riskproject.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleException(GlobalException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getStatus(), e.getMessage()));
    }
}
