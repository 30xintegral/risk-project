package com.demo.riskproject.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
