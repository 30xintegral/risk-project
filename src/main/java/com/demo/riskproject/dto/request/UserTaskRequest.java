package com.demo.riskproject.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserTaskRequest {
    @NotNull(message = "Task ID is required")
    private Long taskId;

    @NotEmpty(message = "At least one user ID is required")
    private Set<Long> userIds;

    private Boolean isCompleted = false;

    private String projectUrl;
}
