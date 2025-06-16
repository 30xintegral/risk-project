package com.demo.riskproject.repository;

import com.demo.riskproject.entity.Task;
import com.demo.riskproject.entity.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    Optional<UserTask> findById(Long id);

    Page<UserTask> findAllByUserIdAndIsCompleted(Long userId,Boolean isCompleted ,Pageable pageable);

//    Page<UserTask> findAllByUserId(Long userId,Pageable pageable);

    List<UserTask> findAllByUserIdAndIsCompleted(Long userId,Boolean isCompleted);

    Page<UserTask> findAllByDeadlineIsAfter(LocalDateTime deadlineAfter, Pageable pageable);

    @Query(value = """
    SELECT 
        EXTRACT(YEAR FROM submitted_at) AS year,
        EXTRACT(MONTH FROM submitted_at) AS month,
        COUNT(*) AS count
    FROM user_tasks
    WHERE user_id = :userId
      AND is_completed = true
      AND submitted_at BETWEEN :startDate AND :endDate
    GROUP BY year, month
    ORDER BY year, month
""", nativeQuery = true)
    List<Object[]> findMonthlySubmissionStatsInRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT 
        YEAR(ut.submittedAt) AS year,
        MONTH(ut.submittedAt) AS month,
        SUM(ut.task.point) AS totalBonus
    FROM UserTask ut
    WHERE ut.user.id = :userId
      AND ut.isCompleted = true
      AND ut.submittedAt BETWEEN :start AND :end
    GROUP BY YEAR(ut.submittedAt), MONTH(ut.submittedAt)
    ORDER BY year, month
""")
    List<Object[]> findMonthlyBonusStatsInRange(@Param("userId") Long userId,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);
}
