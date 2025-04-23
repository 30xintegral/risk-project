package com.demo.riskproject.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    private String username;
    private String email;
    private String password;
}
