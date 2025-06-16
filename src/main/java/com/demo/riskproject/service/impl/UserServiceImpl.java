package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.UserListResponse;
import com.demo.riskproject.entity.User;
import com.demo.riskproject.entity.UserPrincipal;
import com.demo.riskproject.repository.UserRepository;
import com.demo.riskproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));

        UserPrincipal userPrincipal = new UserPrincipal();

        userPrincipal.setId(user.getId());
        userPrincipal.setUsername(user.getUsername());
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
        userPrincipal.setUser(user);

        return userPrincipal;
    }

    @Override
    public Integer getUserBalance(Long userId) {
        return userRepository.findBalanceById(userId);
    }

    @Override
    public PaginationResponse<UserListResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> users = userRepository.findAll(pageable);

        List<User> userList = users.getContent();
        List<UserListResponse> userListResponses = new ArrayList<>();


        PaginationResponse<UserListResponse> paginationResponse = new PaginationResponse<>();

        paginationResponse.setPageNumber(pageable.getPageNumber());
        paginationResponse.setPageSize(pageable.getPageSize());
        paginationResponse.setTotalElements(users.getTotalElements());
        paginationResponse.setTotalPages(users.getTotalPages());

        for (User user : userList) {
            UserListResponse userListResponse = new UserListResponse();
            userListResponse.setId(user.getId());
            userListResponse.setUsername(user.getUsername());
            userListResponse.setRoleSet(user.getRoles());
            userListResponses.add(userListResponse);
        }
        paginationResponse.setData(userListResponses);

        return paginationResponse;
    }
}
