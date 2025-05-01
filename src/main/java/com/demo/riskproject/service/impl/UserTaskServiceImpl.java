package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.SingleUserTaskSubmission;
import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.entity.Task;
import com.demo.riskproject.entity.User;
import com.demo.riskproject.entity.UserTask;
import com.demo.riskproject.exception.NotFoundException;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.mapper.TaskMapper;
import com.demo.riskproject.mapper.UserTaskMapper;
import com.demo.riskproject.repository.TaskRepository;
import com.demo.riskproject.repository.UserRepository;
import com.demo.riskproject.repository.UserTaskRepository;
import com.demo.riskproject.service.UserTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserTaskServiceImpl implements UserTaskService {
    private final UserTaskRepository userTaskRepository;
    private final TaskRepository taskRepository;
    private final UserTaskMapper userTaskMapper;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final S3Client s3Client;


    @Value("${aws_s3_bucket_name}")
    private String bucketName;

    private final String s3ProjectsUrl = "https://30xinte-test.s3.eu-north-1.amazonaws.com/projects/";



    @Override
    public List<UserTaskResponse> getCompletedUserTasks(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserTask> userTaskPage = userTaskRepository.findAllByUserIdAndIsCompleted(userId, true, pageable);
        List<UserTask> userTasks = userTaskPage.getContent();
        List<UserTaskResponse> userTaskResponses = new ArrayList<>();
        for (UserTask userTask : userTasks) {
            UserTaskResponse userTaskResponse = userTaskMapper.toResponse(userTask);
            userTaskResponse.setProjectUrl(s3ProjectsUrl +userTask.getProjectUrl());
            Task task = userTask.getTask();
            TaskResponse taskResponse = taskMapper.toResponse(task);
            userTaskResponse.setTask(taskResponse);
            userTaskResponses.add(userTaskResponse);
        }
        return userTaskResponses;
    }

    @Override
    public List<UserTaskResponse> getIncompleteUserTasks(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserTask> userTaskPage = userTaskRepository.findAllByUserIdAndIsCompleted(userId, false, pageable);
        List<UserTask> userTasks = userTaskPage.getContent();
        List<UserTaskResponse> userTaskResponses = new ArrayList<>();
        for (UserTask userTask : userTasks) {
            UserTaskResponse userTaskResponse = userTaskMapper.toResponse(userTask);
            Task task = userTask.getTask();
            TaskResponse taskResponse = taskMapper.toResponse(task);
            userTaskResponse.setTask(taskResponse);
            userTaskResponses.add(userTaskResponse);
        }
        return userTaskResponses;
    }

    @Override
    public void assignTasktoUsers(UserTaskRequest userTaskRequest) {
        Task task = taskRepository.findById(userTaskRequest.getTaskId()).orElseThrow(() -> new NotFoundException("Task not found"));
        List<User> users = userRepository.findAllById(userTaskRequest.getUserIds());
        //here we need to also inform assigner if there is no specific user id found.
        List<UserTask> userTasks = users.stream()
                .map(user -> {
                    UserTask userTask = userTaskMapper.toEntity(userTaskRequest);
                    userTask.setTask(task);
                    userTask.setSubmittedAt(LocalDateTime.now());
                    //projecturl is going to be null by default
                    return userTask;
                }).toList();
        log.info("user tasks list is ready");
        userTaskRepository.saveAll(userTasks);
    }

    @Override
    public void submitTask(SingleUserTaskSubmission singleUserTaskSubmission) {
        User user = userRepository.findById(singleUserTaskSubmission.getUserId()).orElseThrow(()-> new NotFoundException("User not found"));
        List<UserTask> userTasks = userTaskRepository.findAllByUserIdAndIsCompleted(singleUserTaskSubmission.getUserId(), true);
        Task task = new Task();
        UserTask userTask = new UserTask();
        for (UserTask userTaskIterator : userTasks) {
            if (userTaskIterator.getTask().getId().equals(singleUserTaskSubmission.getTaskId())) {
                task=userTaskIterator.getTask();
                userTask=userTaskIterator;
                break;
            }
            throw new NotFoundException("Task not found");
        }
        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("projects/"+singleUserTaskSubmission.getProject().getOriginalFilename())
                    .contentType(singleUserTaskSubmission.getProject().getContentType())
                    .build();
            InputStream inputStream = new BufferedInputStream(singleUserTaskSubmission.getProject().getInputStream());
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, singleUserTaskSubmission.getProject().getSize()));

        } catch (Exception e) {
            throw new TerminatedException("An error occurred while uploading task project");
        }
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setProjectUrl(singleUserTaskSubmission.getProject().getOriginalFilename());
        userTask.setSubmittedAt(LocalDateTime.now());
        userTask.setIsCompleted(true);
        user.addUserTask(userTask);
        userTaskRepository.save(userTask);
        userRepository.save(user);
    }

}
