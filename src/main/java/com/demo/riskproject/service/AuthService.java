package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.LoginDTO;
import com.demo.riskproject.dto.request.RegistrationDTO;
import com.demo.riskproject.dto.request.TokenRequest;
import com.demo.riskproject.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginDTO loginDTO);

    TokenResponse refresh(TokenRequest tokenRequest);

    void register(RegistrationDTO registrationDTO);

    void registerAdmin(RegistrationDTO registrationDTO);

    void registerContentManager(RegistrationDTO registrationDTO);


}
