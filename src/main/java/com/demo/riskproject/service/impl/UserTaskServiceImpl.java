package com.demo.riskproject.service.impl;

import com.demo.riskproject.entity.UserTask;
import com.demo.riskproject.repository.UserTaskRepository;
import com.demo.riskproject.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTaskServiceImpl implements UserTaskService {
    private final UserTaskRepository userTaskRepository;

    @Override
    public List<UserTask> getCompletedUserTasks(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserTask> userTaskPage = userTaskRepository.findAllByUserIdAndIsCompleted(userId, true, pageable);
        return userTaskPage.getContent();
    }

    @Override
    public List<UserTask> getIncompleteUserTasks(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserTask> userTaskPage = userTaskRepository.findAllByUserIdAndIsCompleted(userId, false, pageable);
        return userTaskPage.getContent();
    }
}
