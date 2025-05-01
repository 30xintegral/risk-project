package com.demo.riskproject.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class NotFoundException extends GlobalException {
    private String message;

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
        this.message = message;
    }

}
