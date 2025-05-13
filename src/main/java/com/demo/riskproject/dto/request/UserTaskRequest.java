package com.demo.riskproject.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskRequest {
    private Long taskId;

    private Set<Long> userIds;

    private Boolean isCompleted;

    private String projectUrl;
}
