package com.demo.riskproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class RiskProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskProjectApplication.class, args);
    }

}
