package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.SingleUserTaskSubmission;
import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.entity.UserTask;

import java.util.List;
import java.util.Set;

public interface UserTaskService {
    List<UserTaskResponse> getCompletedUserTasks(Long userId, int page, int size);

    List<UserTaskResponse> getIncompleteUserTasks(Long userId, int page, int size);

    void assignTasktoUsers(UserTaskRequest userTaskRequest);

    void submitTask(SingleUserTaskSubmission singleUserTaskSubmission);
}
