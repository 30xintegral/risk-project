package com.demo.riskproject.repository;

import com.demo.riskproject.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Optional<News> findById(Long id);

    Page<News> findByTitleContaining(String title, Pageable pageable);

    Page<News> findAll(Pageable pageable);
}
