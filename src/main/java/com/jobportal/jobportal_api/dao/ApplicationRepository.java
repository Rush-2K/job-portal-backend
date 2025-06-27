package com.jobportal.jobportal_api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jobportal.jobportal_api.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByJobs_IdAndUser_Id(Long jobId, Long userId);

    List<Application> findByUser_Id(Long userId);

    List<Application> findByJobs_Id(Long jobId);

    Optional<Application> findByIdAndUser_Id(Long applicationId, Long userId);

    @Query("SELECT a FROM Application a JOIN FETCH a.jobs j JOIN FETCH a.user u WHERE a.id = :applicationId")
    Application findApplicationDetailsWithId(Long applicationId);
}
