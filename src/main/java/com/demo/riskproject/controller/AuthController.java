package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.LoginDTO;
import com.demo.riskproject.dto.request.RegistrationDTO;
import com.demo.riskproject.dto.request.TokenRequest;
import com.demo.riskproject.dto.response.TokenResponse;
import com.demo.riskproject.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "https://intranet-banking.vercel.app/", allowCredentials = "true")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        log.info("IP Address: {}", ipAddress);
        log.info("calling auth login service");
        return authService.login(loginDTO);
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        log.info("IP Address: {}", ipAddress);
        log.info("calling auth register service");
        authService.register(registrationDTO);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(TokenRequest tokenRequest) {
        log.info("refreshing token");
        return authService.refresh(tokenRequest);
    }
}
