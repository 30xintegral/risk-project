package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Slf4j
@CrossOrigin(origins = "https://intranet-banking.vercel.app/", allowCredentials = "true")
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public TaskResponse getTask(@PathVariable(name = "id") Long taskId) {
        log.info("Getting task: {}", taskId);
        return taskService.getTaskById(taskId);
    }
    @PostMapping("/add-task")
    public void addTask(@RequestBody @Valid TaskRequest taskRequest) {
        log.info("Adding task: {}", taskRequest);
        taskService.addTask(taskRequest);
        log.info("Task added: {}", taskRequest);
    }

    @GetMapping("/all")
    public PaginationResponse<TaskResponse> getAllTasks(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        log.info("Getting all tasks");
        return taskService.findAllTasks(page, size);
    }

}
