package com.demo.riskproject.controller;

import com.demo.riskproject.entity.UserPrincipal;
import com.demo.riskproject.repository.UserRepository;
import com.demo.riskproject.service.UserService;
import com.demo.riskproject.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "https://intranet-banking.vercel.app", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    @GetMapping("/balance")
    public ResponseEntity<Integer> getBalance() {
        Long userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.getUserBalance(userId));
    }
}
