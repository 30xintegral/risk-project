package com.demo.riskproject.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskRequest {
    @NotNull
    private Long taskId;

    @NotNull
    private Set<Long> userIds;

    @NotNull
    private LocalDateTime deadline;
}
