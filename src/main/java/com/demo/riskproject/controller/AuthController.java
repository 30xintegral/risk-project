package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.LoginDTO;
import com.demo.riskproject.dto.request.RegistrationDTO;
import com.demo.riskproject.dto.request.TokenRequest;
import com.demo.riskproject.dto.response.TokenResponse;
import com.demo.riskproject.security.JwtTokenProvider;
import com.demo.riskproject.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "https://intranet-banking.vercel.app/", allowCredentials = "true")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        log.info("IP Address: {}", ipAddress);
        log.info("calling auth login service");

        Authentication authentication = authService.login(loginDTO);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenResponse tokenResponse = jwtTokenProvider.generateToken(authentication);

        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        log.info("access token: {}", accessToken);
        log.info("refresh token: {}", refreshToken);
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true) //for prod bc it is needed for https not http
                .sameSite("None") //Sent for top-level navigations and some GET requests (good balance).
                .path("/")
                .maxAge(900)  // 900 seconds = 15 minutes
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true) //for prod bc it is needed for https not http
                .sameSite("None") //Sent for top-level navigations and some GET requests (good balance).
                .path("/")
                .maxAge(3600) // 3600 seconds = 1 hour
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok("Login successful");
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
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        log.info("refreshing token");
        String oldRefreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    oldRefreshToken = cookie.getValue();
                    break;
                }
            }
        }
        TokenResponse tokenResponse = authService.refresh(oldRefreshToken);
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        log.info("access token: {}", accessToken);
        log.info("refresh token: {}", refreshToken);
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true) //for prod =true, bc it is needed for https not http
                .sameSite("Lax") //Sent for top-level navigations and some GET requests (good balance).
                .path("/")
                .maxAge(900)  // 900 seconds = 15 minutes
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true) //for prod =true, bc it is needed for https not http
                .sameSite("Lax") //Sent for top-level navigations and some GET requests (good balance).
                .path("/")
                .maxAge(3600) // 3600 seconds = 1 hour
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok("Refresh successful");
    }
}
