package com.demo.riskproject.exception;

import org.springframework.http.HttpStatus;

public class TerminatedException extends GlobalException {
  private final String message;

  public TerminatedException(String message) {
      super(HttpStatus.BAD_REQUEST, message);
      this.message = message;
  }
}
