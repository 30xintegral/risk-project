package com.demo.riskproject.controller;

import com.demo.riskproject.dto.response.*;
import com.demo.riskproject.entity.User;
import com.demo.riskproject.entity.UserPrincipal;
import com.demo.riskproject.exception.NotFoundException;
import com.demo.riskproject.exception.TerminatedException;
import com.demo.riskproject.repository.UserRepository;
import com.demo.riskproject.service.UserService;
import com.demo.riskproject.service.UserTaskService;
import com.demo.riskproject.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@CrossOrigin(origins = "https://intranet-banking.vercel.app/", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final UserTaskService userTaskService;

    @GetMapping("/balance")
    public ResponseEntity<Integer> getBalance() {
        Long userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.getUserBalance(userId));
    }
    @GetMapping("/info")
    public ResponseEntity<UserInfo> getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
            throw new TerminatedException("User not authenticated");
        }

        User user = userPrincipal.getUser();

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(user.getUsername());
        userInfo.setUserId(user.getId());
        userInfo.setRole(user.getRoles());

        return ResponseEntity.ok(userInfo);
    }
    @GetMapping("/all")
    public PaginationResponse<UserListResponse> userList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("All users fetch started");
        return userService.findAll(page, size);
    }

    @GetMapping("/monthly-bonus")
    public List<UserMonthlyBonusStat> getMonthlyBonusStats(
            @RequestParam String from,  // format: 2024-02
            @RequestParam String to     // format: 2025-06
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userDetails) {
            userId = userDetails.getId();
        }
        if (userId == null) {
            throw new NotFoundException("User not found");
        }

        YearMonth fromMonth = YearMonth.parse(from);
        YearMonth toMonth = YearMonth.parse(to);

        return userTaskService.getMonthlyBonusStatsInRange(userId, fromMonth, toMonth);
    }
    @GetMapping("/submission-stats")
    public ResponseEntity<List<UserMonthlySubmissionStat>> getStatsBetweenMonths(
            @RequestParam String from,  // format: 2024-02
            @RequestParam String to     // format: 2025-06
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userDetails) {
            userId = userDetails.getId();
        }
        if (userId == null) {
            throw new NotFoundException("User not found");
        }

        YearMonth fromMonth = YearMonth.parse(from);
        YearMonth toMonth = YearMonth.parse(to);

        List<UserMonthlySubmissionStat> stats = userTaskService.getMonthlyStatsInRange(userId, fromMonth, toMonth);
        return ResponseEntity.ok(stats);
    }
}
