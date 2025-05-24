package com.demo.riskproject.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskResponse {
    private Long id;
    private Long userId;
    private TaskResponse task;
    private Boolean isCompleted;
    private String projectUrl;
    private LocalDateTime submittedAt;
    private LocalDateTime deadline;

}
