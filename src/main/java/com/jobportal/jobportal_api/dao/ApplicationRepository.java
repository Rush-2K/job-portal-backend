package com.jobportal.jobportal_api.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.jobportal_api.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByJobs_IdAndUser_Id(Long jobId, Long userId);

}
