package com.demo.riskproject.service.impl;

import com.demo.riskproject.entity.Task;
import com.demo.riskproject.entity.UserTask;
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

    @Override
    public Set<Task> getIncompleteTasksByUser(Long userId, int page, int size) {
        List<UserTask> userTasks = userTaskService.getIncompleteUserTasks(userId, page, size);
        Set<Task> taskSet = new HashSet<>();
        for (UserTask userTask : userTasks) {
            taskSet.add(userTask.getTask());
        }
        return taskSet;
    }
}
