package com.demo.riskproject.entity;

import com.demo.riskproject.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<UserTask> userTasks = new HashSet<>();

    @Column(nullable = false)
    private Integer balance;

    private Boolean deleted;

    private Boolean locked;

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addUserTask(UserTask userTask) {
        this.userTasks.add(userTask);
    }
}
