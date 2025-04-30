package com.demo.riskproject.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserTaskResponse {
    private Long id;
    private Long userId;
    private TaskResponse task;
    private Boolean isCompleted;
    private String projectUrl;
    private LocalDateTime submittedAt;

}
