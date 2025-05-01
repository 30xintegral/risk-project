package com.demo.riskproject.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GlobalException extends RuntimeException {
    private HttpStatus status;

    public GlobalException(HttpStatus status, String message) {
        super(message);
        this.status = status;

    }
}
