package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.*;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.service.AuthService;
import com.demo.riskproject.service.NewsService;
import com.demo.riskproject.service.TaskService;
import com.demo.riskproject.service.UserTaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api-v1/")
@Slf4j
public class AdminController {
    private final AuthService authService;
    private final TaskService taskService;
    private final NewsService newsService;
    private final UserTaskService userTaskService;

    @PostMapping("/register-admin")
    public void registerAdmin(@RequestBody @Valid RegistrationDTO registrationDTO, HttpServletRequest request) {
        log.info("admin register service called from ip: "+ request.getRemoteAddr());
        authService.registerAdmin(registrationDTO);
    }
    @PostMapping("/register-content-manager")
    public void registerContentManager(@RequestBody @Valid RegistrationDTO registrationDTO, HttpServletRequest request) {
        log.info("content manager register service called from ip:" + request.getRemoteAddr());
        authService.registerContentManager(registrationDTO);
    }
    @PostMapping("/add-task")
    public void addTask(@RequestBody @Valid TaskRequest taskRequest) {
        log.info("Adding task: {}", taskRequest);
        taskService.addTask(taskRequest);
        log.info("Task added: {}", taskRequest);
    }
    @GetMapping("/task")
    public TaskResponse getTask(@RequestParam(name = "id") Long taskId) {
        log.info("Getting task: {}", taskId);
        return taskService.getTaskById(taskId);
    }
    @PostMapping("/assign")
    public void assignTaskToUsers(@RequestBody @Valid UserTaskRequest userTaskRequest) {
        userTaskService.assignTasktoUsers(userTaskRequest);
    }

}
