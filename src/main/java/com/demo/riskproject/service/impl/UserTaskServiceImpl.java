package com.demo.riskproject.service.impl;

import com.demo.riskproject.dto.request.SingleUserTaskSubmission;
import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserTaskServiceImpl implements UserTaskService {
    private final UserTaskRepository userTaskRepository;
    private final TaskRepository taskRepository;
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
        User user = userPrincipal.getUser();

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

            String key = String.format("user-%d/task-%d/%s", user.getId(), userTask.getTask().getId(), userTask.getProjectUrl());
            String projectUrl = (userTask.getProjectUrl() == null)
                    ? null
                    : s3ProjectsUrl + key;

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
        userTaskPaginationResponse.getData().sort(deadlineComparator);
        return userTaskPaginationResponse;
    }

    @Override
    public void assignTaskToUsers(UserTaskRequest userTaskRequest) {
        Task task = taskRepository.findById(userTaskRequest.getTaskId()).orElseThrow(() -> new NotFoundException("Task not found"));
        List<User> users = userRepository.findAllById(userTaskRequest.getUserIds());

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

                    return userTask;
                }).toList();
        log.info("user tasks list is ready");
        userTaskRepository.saveAll(userTasks);
    }

    @Override
    public List<UserMonthlySubmissionStat> getMonthlyStatsInRange(Long userId, YearMonth from, YearMonth to) {
        LocalDateTime start = from.atDay(1).atStartOfDay();
        LocalDateTime end = to.atEndOfMonth().atTime(23, 59, 59);

        List<Object[]> rawStats = userTaskRepository.findMonthlySubmissionStatsInRange(userId, start, end);

        Map<YearMonth, Long> statsMap = new HashMap<>();
        for (Object[] row : rawStats) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            long count = ((Number) row[2]).longValue();
            statsMap.put(YearMonth.of(year, month), count);
        }

        List<UserMonthlySubmissionStat> results = new ArrayList<>();
        YearMonth current = from;
        while (!current.isAfter(to)) {
            long count = statsMap.getOrDefault(current, 0L);
            results.add(new UserMonthlySubmissionStat(current.getYear(), current.getMonthValue(), count));
            current = current.plusMonths(1);
        }

        return results;
    }

    @Override
    public void submitTask(SingleUserTaskSubmission singleUserTaskSubmission) {
        log.info("submission started");
        User user = userRepository.findById(singleUserTaskSubmission.getUserId()).orElseThrow(()-> new NotFoundException("User not found"));
        log.info("user found. user id: {}", user.getId());
        List<UserTask> userTasks = userTaskRepository.findAllByUserIdAndIsCompleted(singleUserTaskSubmission.getUserId(), false);
        log.info("user tasks list is fetched. size: {}", userTasks.size());
        UserTask userTask = new UserTask();
        boolean found = false;
        for (UserTask userTaskIterator : userTasks) {
            if (userTaskIterator.getTask() == null) {
                log.warn("Task is null for userTask id {}", userTaskIterator.getId());
                continue;
            }
            log.debug("Comparing {} with {}", userTaskIterator.getTask().getId(), singleUserTaskSubmission.getTaskId());
            if (userTaskIterator.getTask().getId().equals(singleUserTaskSubmission.getTaskId())) {
                userTask = userTaskIterator;
                found = true;
                log.info("task is on the user's list. task id: {}", userTask.getTask().getId());
                break;
            }
        }
        if (!found) {
            throw new NotFoundException("Task not found");
        }
        if (LocalDateTime.now().isAfter(userTask.getDeadline())){
            throw new TerminatedException("It already passed the deadline");
        }
        log.info("creating key prefix");
        String key = String.format("user-%d/task-%d/%s", user.getId(), userTask.getTask().getId(), singleUserTaskSubmission.getProject().getOriginalFilename());
        log.info("key prefix is: {}", key);
        try(InputStream inputStream = new BufferedInputStream(singleUserTaskSubmission.getProject().getInputStream())){
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("projects/"+key)
                    .contentType(singleUserTaskSubmission.getProject().getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, singleUserTaskSubmission.getProject().getSize()));

        } catch (Exception e) {
            throw new TerminatedException("An error occurred while uploading task project");
        }
        log.info("updating user balance");
        int newBalance = user.getBalance() + userTask.getTask().getPoint();
        log.info("setting new balance");
        user.setBalance(newBalance);
        userRepository.save(user);
        log.info("user updated. new balance: {}", newBalance);
        log.info("updating user-task data");
        userTask.setProjectUrl(singleUserTaskSubmission.getProject().getOriginalFilename());
        userTask.setSubmittedAt(LocalDateTime.now());
        userTask.setIsCompleted(true);
        log.info("updating user-task completed");
        userTaskRepository.save(userTask);
        log.info("user task updated");
    }

    @Override
    public List<UserMonthlyBonusStat> getMonthlyBonusStatsInRange(Long userId, YearMonth from, YearMonth to) {
        LocalDateTime start = from.atDay(1).atStartOfDay();
        LocalDateTime end = to.atEndOfMonth().atTime(23, 59, 59);

        List<Object[]> rawStats = userTaskRepository.findMonthlyBonusStatsInRange(userId, start, end);

        Map<YearMonth, Integer> bonusMap = new HashMap<>();
        for (Object[] row : rawStats) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            int totalBonus = ((Number) row[2]).intValue();
            bonusMap.put(YearMonth.of(year, month), totalBonus);
        }

        List<UserMonthlyBonusStat> results = new ArrayList<>();
        YearMonth current = from;
        while (!current.isAfter(to)) {
            int totalBonus = bonusMap.getOrDefault(current, 0);
            results.add(new UserMonthlyBonusStat(current.getYear(), current.getMonthValue(), totalBonus));
            current = current.plusMonths(1);
        }

        return results;
    }

}
