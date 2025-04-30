package com.demo.riskproject.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SingleUserTaskSubmission {
    @NotNull(message = "Task ID is required")
    private Long taskId;

    @NotEmpty(message = "User ID is required")
    private Long userId;


    private MultipartFile project;
}
