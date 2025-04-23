package com.demo.riskproject.service;

import com.demo.riskproject.entity.UserTask;

import java.util.List;

public interface UserTaskService {
    List<UserTask> getCompletedUserTasks(Long userId, int page, int size);

    List<UserTask> getIncompleteUserTasks(Long userId, int page, int size);

}
