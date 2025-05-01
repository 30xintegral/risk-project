package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.RegistrationDTO;
import com.demo.riskproject.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api-v1/")
@Slf4j
public class AdminController {
    private final AuthService authService;

    @PostMapping("/register-admin")
    public void registerAdmin(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request) {
        log.info("admin register service called from ip: "+ request.getRemoteAddr());
        authService.registerAdmin(registrationDTO);
    }
    @PostMapping("/register-content-manager")
    public void registerContentManager(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request) {
        log.info("content manager register service called from ip:" + request.getRemoteAddr());
        authService.registerContentManager(registrationDTO);
    }
}
