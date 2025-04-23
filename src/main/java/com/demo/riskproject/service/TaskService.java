package com.demo.riskproject.service;

import com.demo.riskproject.entity.Task;


import java.util.Set;

public interface TaskService {
    Set<Task> getIncompleteTasksByUser(Long userId, int page, int size);

}
