package com.demo.riskproject.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
