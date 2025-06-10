package com.demo.riskproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserMonthlySubmissionStat {
    private int year;
    private int month;
    private long submissions;

}
