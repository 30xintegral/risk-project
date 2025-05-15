package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.entity.Task;


import java.util.Set;

public interface TaskService {

    void addTask(TaskRequest taskRequest);

    TaskResponse getTaskById(Long taskId);
}
