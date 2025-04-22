package com.demo.riskproject.repository;

import com.demo.riskproject.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    List<UserTask> findAllByUserId(Long userId);

    Optional<UserTask> findById(Long id);
}
