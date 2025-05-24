package com.demo.riskproject.service.comparators;

import com.demo.riskproject.dto.response.UserTaskResponse;

import java.util.Comparator;

public class DeadlineComparator implements Comparator<UserTaskResponse> {
    @Override
    public int compare(UserTaskResponse o1, UserTaskResponse o2) {
        return o1.getDeadline().compareTo(o2.getDeadline());
    }
}
