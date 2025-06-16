package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.entity.Task;


import java.util.List;
import java.util.Set;

public interface TaskService {

    void addTask(TaskRequest taskRequest);

    TaskResponse getTaskById(Long taskId);

    PaginationResponse<TaskResponse> findAllTasks(int page, int size);
}
