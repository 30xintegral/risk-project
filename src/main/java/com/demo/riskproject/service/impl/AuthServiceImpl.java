package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.LoginDTO;
import com.demo.riskproject.dto.request.RegistrationDTO;
import com.demo.riskproject.dto.request.TokenRequest;
import com.demo.riskproject.dto.response.TokenResponse;
import com.demo.riskproject.entity.User;
import com.demo.riskproject.enums.Role;
import com.demo.riskproject.exception.NotFoundException;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.repository.UserRepository;
import com.demo.riskproject.security.JwtTokenProvider;
import com.demo.riskproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public TokenResponse login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(() -> new NotFoundException("User not found"));
        if(user.getDeleted() || user.getLocked()) {
            throw new TerminatedException("User is locked or deleted.");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public TokenResponse refresh(TokenRequest tokenRequest) {
        return jwtTokenProvider.refreshToken(tokenRequest.getRefreshToken());
    }

    @Override
    public void register(RegistrationDTO registrationDTO) {
        try{
            User user = new User();
            user.setUsername(registrationDTO.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            user.setLocked(false);
            user.setDeleted(false);
            user.addRole(Role.USER);
            userRepository.save(user);
        }catch (Exception e){
            throw new TerminatedException("Error while creating user");
        }

    }

    @Override
    public void registerAdmin(RegistrationDTO registrationDTO) {
        try{
            User user = new User();
            user.setUsername(registrationDTO.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            user.setLocked(false);
            user.setDeleted(false);
            user.addRole(Role.ADMIN);
            userRepository.save(user);
        }catch (Exception e){
            throw new TerminatedException("Error while creating user");
        }
    }

    @Override
    public void registerContentManager(RegistrationDTO registrationDTO) {
        try{
            User user = new User();
            user.setUsername(registrationDTO.getUsername());
            user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            user.setLocked(false);
            user.setDeleted(false);
            user.addRole(Role.CONTENTMANAGER);
            userRepository.save(user);
        }catch (Exception e){
            throw new TerminatedException("Error while creating user");
        }
    }
}
