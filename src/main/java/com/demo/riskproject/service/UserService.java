package com.demo.riskproject.service;

import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.UserListResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Integer getUserBalance(Long userId);

    PaginationResponse<UserListResponse> findAll(int page, int size);
}
