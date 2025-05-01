package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.service.TaskService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Slf4j
public class TaskController {
    private final TaskService taskService;

    public Set<TaskResponse> getIncompleteTasksByUser(@RequestParam(name = "id") Long userId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return taskService.getIncompleteTasksByUser(userId, page, size);
    }

    public void addTask(@Valid @RequestBody TaskRequest taskRequest) {
        log.info("Adding task: {}", taskRequest);
        taskService.addTask(taskRequest);
        log.info("Task added: {}", taskRequest);
    }

    public TaskResponse getTask(@RequestParam(name = "id") Long taskId) {
        log.info("Getting task: {}", taskId);
        return taskService.getTaskById(taskId);
    }
}
