package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.entity.Task;
import com.demo.riskproject.entity.UserTask;
import com.demo.riskproject.mapper.UserTaskMapper;
import com.demo.riskproject.repository.TaskRepository;
import com.demo.riskproject.service.TaskService;
import com.demo.riskproject.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserTaskService userTaskService;
    private final UserTaskMapper userTaskMapper;

    @Override
    public Set<TaskResponse> getIncompleteTasksByUser(Long userId, int page, int size) {
        List<UserTaskResponse> userTasks = userTaskService.getIncompleteUserTasks(userId, page, size);
        Set<TaskResponse> taskSet = new HashSet<>();
        for (UserTaskResponse userTaskResponseIterator : userTasks) {
            taskSet.add(userTaskResponseIterator.getTask());
        }
        return taskSet;
    }

    @Override
    public void addTask(TaskRequest taskRequest) {

    }
}
