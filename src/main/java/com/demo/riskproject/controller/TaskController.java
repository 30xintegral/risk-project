package com.demo.riskproject.controller;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.service.TaskService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/incomplete-tasks")
    public Set<TaskResponse> getIncompleteTasksByUser(@RequestParam(name = "id") Long userId, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return taskService.getIncompleteTasksByUser(userId, page, size);
    }

}
