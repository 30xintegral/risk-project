package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.TaskRequest;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.entity.Task;
import com.demo.riskproject.exception.NotFoundException;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.mapper.TaskMapper;
import com.demo.riskproject.mapper.UserTaskMapper;
import com.demo.riskproject.repository.TaskRepository;
import com.demo.riskproject.service.TaskService;
import com.demo.riskproject.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public void addTask(TaskRequest taskRequest) {
        if (!taskRepository.findByNameContainingIgnoreCase(taskRequest.getName()).isEmpty()) {
            throw new TerminatedException("Task similar to this already exists");
        }
        Task task = Task.builder().
                name(taskRequest.getName()).
                description(taskRequest.getDescription()).
                point(taskRequest.getPoint()).
                build();
        taskRepository.save(task);
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
        return TaskResponse.builder().
                id(task.getId()).
                name(task.getName()).
                point(task.getPoint()).
                description(task.getDescription()).
                build();
    }

    @Override
    public PaginationResponse<TaskResponse> findAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<Task> taskList = tasks.getContent();
        List<TaskResponse> taskResponses = new ArrayList<>();
        PaginationResponse<TaskResponse> paginationResponse = new PaginationResponse<>();
        paginationResponse.setTotalElements(tasks.getTotalElements());
        paginationResponse.setTotalPages(tasks.getTotalPages());
        paginationResponse.setPageSize(pageable.getPageSize());
        paginationResponse.setPageNumber(pageable.getPageNumber());

        for (Task task : taskList) {
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setId(task.getId());
            taskResponse.setName(task.getName());
            taskResponse.setDescription(task.getDescription());
            taskResponse.setPoint(task.getPoint());
            taskResponses.add(taskResponse);
        }
        paginationResponse.setData(taskResponses);
        return paginationResponse;
    }
}
