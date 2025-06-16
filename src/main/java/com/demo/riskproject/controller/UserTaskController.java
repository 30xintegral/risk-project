package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.SingleUserTaskSubmission;
import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.UserMonthlySubmissionStat;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.entity.UserPrincipal;
import com.demo.riskproject.exception.NotFoundException;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.service.UserTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Slf4j
@CrossOrigin(origins = "https://intranet-banking.vercel.app/", allowCredentials = "true")
public class UserTaskController {
    private final UserTaskService userTaskService;


    @GetMapping("/get-user-tasks")
    public PaginationResponse<UserTaskResponse> getUserTasks(@RequestParam(name = "isCompleted", required = false) Boolean isCompleted, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("get-user-tasks service called");
        return userTaskService.getUserTasks(isCompleted != null ? isCompleted : false, page, size);
    }

    @PatchMapping("/submit")
    public void submitTask(@ModelAttribute @Valid SingleUserTaskSubmission singleUserTaskSubmission) {
        log.info("submit task service called");
        userTaskService.submitTask(singleUserTaskSubmission);
        log.info("task submission completed");
    }
    @PostMapping("/assign")
    public void assignTaskToUsers(@RequestBody @Valid UserTaskRequest userTaskRequest) {
        log.info("assign task service called");
        userTaskService.assignTaskToUsers(userTaskRequest);
        log.info("task assignment completed");
    }


}
