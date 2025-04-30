package com.demo.riskproject.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String name;
    private String description;
    private Byte point;
}
