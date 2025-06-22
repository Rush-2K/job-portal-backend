package com.jobportal.jobportal_api.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.jobportal_api.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    boolean existsByIdAndUser_Id(Long jobId, Long userId);
}
