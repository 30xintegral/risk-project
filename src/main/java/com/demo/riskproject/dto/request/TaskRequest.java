package com.demo.riskproject.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    @NotNull(message = "name is required")
    private String name;

    private String description;

    @NotNull(message = "Point is required")
    @Min(value = 1, message = "Point must be at least 1")
    @Max(value = 5, message = "Point must be at most 5")
    private Byte point;


}
