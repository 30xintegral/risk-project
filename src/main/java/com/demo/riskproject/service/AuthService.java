package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.LoginDTO;
import com.demo.riskproject.dto.request.RegistrationDTO;
import com.demo.riskproject.dto.request.TokenRequest;
import com.demo.riskproject.dto.response.TokenResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    Authentication login(LoginDTO loginDTO);

    TokenResponse refresh(String refreshToken);

    void register(RegistrationDTO registrationDTO);

    void registerAdmin(RegistrationDTO registrationDTO);

    void registerContentManager(RegistrationDTO registrationDTO);


}
