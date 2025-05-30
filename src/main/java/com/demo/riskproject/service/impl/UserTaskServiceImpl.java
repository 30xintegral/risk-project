package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.SingleUserTaskSubmission;
import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.TaskResponse;
import com.demo.riskproject.dto.response.UserTaskResponse;
import com.demo.riskproject.entity.Task;
import com.demo.riskproject.entity.User;
import com.demo.riskproject.entity.UserPrincipal;
import com.demo.riskproject.entity.UserTask;
import com.demo.riskproject.exception.NotFoundException;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.mapper.TaskMapper;
import com.demo.riskproject.mapper.UserTaskMapper;
import com.demo.riskproject.repository.TaskRepository;
import com.demo.riskproject.repository.UserRepository;
import com.demo.riskproject.repository.UserTaskRepository;
import com.demo.riskproject.service.UserTaskService;
import com.demo.riskproject.service.comparators.DeadlineComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @Value("${aws_s3_bucket_projects_url}")
    private String s3ProjectsUrl;



    @Override
    public PaginationResponse<UserTaskResponse> getUserTasks(Boolean isCompleted, int page, int size) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<UserTask> userTaskPage = userTaskRepository.findAllByUserIdAndIsCompleted(userId, isCompleted, pageable);
        List<UserTask> userTasks = userTaskPage.getContent();

        PaginationResponse<UserTaskResponse> userTaskPaginationResponse = new PaginationResponse<>();
        userTaskPaginationResponse.setPageNumber(userTaskPage.getNumber());
        userTaskPaginationResponse.setPageSize(userTaskPage.getSize());
        userTaskPaginationResponse.setTotalPages(userTaskPage.getTotalPages());
        userTaskPaginationResponse.setTotalElements(userTaskPage.getTotalElements());

        for (UserTask userTask : userTasks) {
            Task task = userTask.getTask();
            TaskResponse taskResponse = TaskResponse.builder().
                    id(task.getId()).
                    description(task.getDescription()).
                    name(task.getName()).
                    point(task.getPoint()).
                    build();

            String projectUrl = (userTask.getProjectUrl() == null)
                    ? null
                    : s3ProjectsUrl + userTask.getProjectUrl();

            UserTaskResponse userTaskResponse = UserTaskResponse.builder().
                    isCompleted(userTask.getIsCompleted()).
                    task(taskResponse).
                    userId(userId).
                    id(userTask.getId()).
                    submittedAt(userTask.getSubmittedAt()).
                    projectUrl(projectUrl).
                    deadline(userTask.getDeadline()).
                    build();

            userTaskPaginationResponse.addData(userTaskResponse);
        }
        DeadlineComparator deadlineComparator = new DeadlineComparator();
        userTaskPaginationResponse.getData().sort(deadlineComparator); //here we created a deadline comparator for sorting it in ascending order
        return userTaskPaginationResponse;
    }

    @Override
    public void assignTaskToUsers(UserTaskRequest userTaskRequest) {
        Task task = taskRepository.findById(userTaskRequest.getTaskId()).orElseThrow(() -> new NotFoundException("Task not found"));
        List<User> users = userRepository.findAllById(userTaskRequest.getUserIds());
        //here we also need to inform assigner if there is no specific user id found.
        if (userTaskRequest.getDeadline() == null) {
            throw new TerminatedException("Deadline must not be null");
        }
        List<UserTask> userTasks = users.stream()
                .map(user -> {
                    UserTask userTask = UserTask.builder().
                    isCompleted(false).
                    task(task).
                    deadline(userTaskRequest.getDeadline()).
                    user(user).build();

                    //projecturl is going to be null by default
                    return userTask;
                }).toList();
        log.info("user tasks list is ready");
        userTaskRepository.saveAll(userTasks);
    }

    @Override
    public void submitTask(SingleUserTaskSubmission singleUserTaskSubmission) {
        User user = userRepository.findById(singleUserTaskSubmission.getUserId()).orElseThrow(()-> new NotFoundException("User not found"));
        List<UserTask> userTasks = userTaskRepository.findAllByUserIdAndIsCompleted(singleUserTaskSubmission.getUserId(), false);
        UserTask userTask = new UserTask();
        boolean found = false;
        for (UserTask userTaskIterator : userTasks) {
            if (userTaskIterator.getTask().getId().equals(singleUserTaskSubmission.getTaskId())) {
                userTask = userTaskIterator;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NotFoundException("Task not found");
        }
        if (LocalDateTime.now().isAfter(userTask.getDeadline())){
            throw new TerminatedException("It already passed the deadline");
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
        int newBalance = user.getBalance() + userTask.getTask().getPoint();
        user.setBalance(newBalance);
        userTask.setProjectUrl(singleUserTaskSubmission.getProject().getOriginalFilename());
        userTask.setSubmittedAt(LocalDateTime.now());
        userTask.setIsCompleted(true);

        userTaskRepository.save(userTask);
    }

}
