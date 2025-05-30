package com.demo.riskproject.repository;

import com.demo.riskproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);


    Optional<User> findByUsername(String username);

    @Query("SELECT u.balance FROM User u WHERE u.id = :id")
    Integer findBalanceById(@Param("id") Long id);
}
