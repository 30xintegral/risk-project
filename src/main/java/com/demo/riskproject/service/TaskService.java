package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.entity.Task;


import java.util.Set;

public interface TaskService {
    Set<TaskResponse> getIncompleteTasksByUser(Long userId, int page, int size);

    void addTask(TaskRequest taskRequest);

    TaskResponse getTaskById(Long taskId);
}
