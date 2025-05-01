package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.SingleUserTaskSubmission;
import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.service.UserTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class UserTaskController {
    private final UserTaskService userTaskService;

    @GetMapping("/get-completed-tasks")
    public List<UserTaskResponse> getCompletedUserTasks(@RequestParam(name = "id") Long userId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return userTaskService.getCompletedUserTasks(userId, page, size);
    }

    @GetMapping("/get-incomplete-user-tasks")
    public List<UserTaskResponse> getIncompleteUserTasks(@RequestParam(name = "id") Long userId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return userTaskService.getIncompleteUserTasks(userId, page, size);
    }

    @PostMapping("/assign")
    public void assignTaskToUsers(@Valid @RequestBody UserTaskRequest userTaskRequest) {
        userTaskService.assignTasktoUsers(userTaskRequest);
    }

    @PostMapping("/submit")
    public void submitTask(@Valid @RequestBody SingleUserTaskSubmission singleUserTaskSubmission) {
        userTaskService.submitTask(singleUserTaskSubmission);
    }
}
