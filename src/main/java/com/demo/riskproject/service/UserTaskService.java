package com.demo.riskproject.service;

import com.demo.riskproject.dto.request.SingleUserTaskSubmission;
import com.demo.riskproject.dto.request.UserTaskRequest;
import com.demo.riskproject.dto.response.PaginationResponse;
import com.demo.riskproject.dto.response.UserMonthlyBonusStat;
import com.demo.riskproject.dto.response.UserMonthlySubmissionStat;
import com.demo.riskproject.dto.response.UserTaskResponse;

import java.time.YearMonth;
import java.util.List;

public interface UserTaskService {
    PaginationResponse<UserTaskResponse> getUserTasks(Boolean isCompleted, int page, int size);

    void assignTaskToUsers(UserTaskRequest userTaskRequest);

    void submitTask(SingleUserTaskSubmission singleUserTaskSubmission);

    List<UserMonthlySubmissionStat> getMonthlyStatsInRange(Long userId, YearMonth from, YearMonth to);

    List<UserMonthlyBonusStat> getMonthlyBonusStatsInRange(Long userId, YearMonth from, YearMonth to);
}
