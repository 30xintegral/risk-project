package com.demo.riskproject.dto.response;

import com.demo.riskproject.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserListResponse {
    private Long id;

    private String username;

    private Set<Role> roleSet;
}
