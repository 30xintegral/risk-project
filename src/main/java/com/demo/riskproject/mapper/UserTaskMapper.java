package com.demo.riskproject.mapper;

import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.entity.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserTaskMapper {
    UserTask toEntity(UserTaskRequest userTaskRequest);

    UserTaskResponse toResponse(UserTask userTask);
}
