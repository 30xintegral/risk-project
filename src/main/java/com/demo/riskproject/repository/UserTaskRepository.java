package com.demo.riskproject.repository;

import com.demo.riskproject.entity.Task;
import com.demo.riskproject.entity.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
