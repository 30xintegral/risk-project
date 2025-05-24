package com.demo.riskproject.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SingleUserTaskSubmission {
    @NotNull
    private Long taskId;

    @NotNull
    private Long userId;

    @NotNull
    private MultipartFile project;
}
