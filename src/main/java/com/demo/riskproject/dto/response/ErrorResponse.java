package com.demo.riskproject.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private final LocalDateTime timeStamp = LocalDateTime.now();

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}