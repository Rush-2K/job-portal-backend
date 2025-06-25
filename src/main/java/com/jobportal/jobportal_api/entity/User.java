package com.jobportal.jobportal_api.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdTime;

    @Column(name = "updated_at")
    private LocalDateTime updatedTime;

    @OneToMany(mappedBy = "user")
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Application> applications = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Bookmark> bookmarks = new HashSet<>();
}
