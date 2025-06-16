package com.demo.riskproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMonthlyBonusStat {
    private int year;
    private int month;
    private int totalBonus;
}
