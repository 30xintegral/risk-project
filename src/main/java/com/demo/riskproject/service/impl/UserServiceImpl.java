package com.demo.riskproject.service.impl;

import com.demo.riskproject.entity.User;
import com.demo.riskproject.entity.UserPrincipal;
import com.demo.riskproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        UserPrincipal userPrincipal = new UserPrincipal();

        userPrincipal.setId(user.getId());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setEmail(user.getEmail());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setUser(user);
        userPrincipal.setRoles(user.getRoles());
        userPrincipal.setLocked(user.getLocked());
        userPrincipal.setDeleted(user.getDeleted());

        return userPrincipal;
    }
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setId(user.getId());
        userPrincipal.setRoles(user.getRoles());
        userPrincipal.setLocked(user.getLocked());
        userPrincipal.setDeleted(user.getDeleted());
        userPrincipal.setEmail(user.getEmail());
        userPrincipal.setUser(user);

        return userPrincipal;
    }

}
