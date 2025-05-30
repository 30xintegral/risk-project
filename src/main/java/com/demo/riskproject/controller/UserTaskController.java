package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.SingleUserTaskSubmission;
import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.service.UserTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@CrossOrigin(origins = "https://intranet-banking.vercel.app/", allowCredentials = "true")
public class UserTaskController {
    private final UserTaskService userTaskService;


    @GetMapping("/get-user-tasks")
    public PaginationResponse<UserTaskResponse> getUserTasks(@RequestParam(name = "isCompleted", required = false) Boolean isCompleted, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return userTaskService.getUserTasks(isCompleted != null ? isCompleted : false, page, size);
    }

    @PatchMapping("/submit")
    public void submitTask(@ModelAttribute @Valid SingleUserTaskSubmission singleUserTaskSubmission) {
        userTaskService.submitTask(singleUserTaskSubmission);
    }
    @PostMapping("/assign")
    public void assignTaskToUsers(@RequestBody @Valid UserTaskRequest userTaskRequest) {
        userTaskService.assignTaskToUsers(userTaskRequest);
    }
}
