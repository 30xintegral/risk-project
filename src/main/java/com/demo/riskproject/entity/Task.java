package com.demo.riskproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 128)
    private String description;

    @Column(nullable = false)
    @Min(value = 1, message = "Point must be at least 1")
    @Max(value = 5, message = "Point must be at most 5")
    private Integer point;

    @OneToMany(mappedBy = "task")
    private Set<UserTask> userTasks = new HashSet<>();

}
