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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "jobs")
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "companyName")
    private String companyName;

    @Column(name = "salary")
    private Long salary;

    @Column(name = "jobType")
    private String jobType;

    @Column(name = "jobStatus")
    private boolean jobStatus;

    // FK posted by
    @ManyToOne
    @JoinColumn(name = "posted_by", referencedColumnName = "id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdTime;

    @Column(name = "updated_at")
    private LocalDateTime updatedTime;

    @OneToMany(mappedBy = "jobs", cascade = CascadeType.ALL)
    private Set<Application> application = new HashSet<>();

    @OneToMany(mappedBy = "jobs", cascade = CascadeType.ALL)
    private Set<Bookmark> bookmark = new HashSet<>();
}
