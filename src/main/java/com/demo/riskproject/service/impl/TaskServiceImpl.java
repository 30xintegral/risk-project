package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.entity.Task;
import com.demo.riskproject.entity.UserTask;
import com.demo.riskproject.exception.NotFoundException;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.mapper.TaskMapper;
import com.demo.riskproject.mapper.UserTaskMapper;
import com.demo.riskproject.repository.TaskRepository;
import com.demo.riskproject.service.TaskService;
import com.demo.riskproject.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserTaskService userTaskService;
    private final UserTaskMapper userTaskMapper;
    private final TaskMapper taskMapper;

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
        if (!taskRepository.findByNameContainingIgnoreCase(taskRequest.getName()).isEmpty()) {
            throw new TerminatedException("Task like this already exists");
        }
        Task task = taskMapper.toEntity(taskRequest);
        taskRepository.save(task);
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("task not found"));
        return taskMapper.toResponse(task);
    }
}
