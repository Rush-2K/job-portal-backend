package com.jobportal.jobportal_api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jobportal.jobportal_api.dto.response.ViewAllJobsResponseDTO;
import com.jobportal.jobportal_api.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

        boolean existsByIdAndUser_Id(Long jobId, Long userId);

        Page<Job> findByJobStatus(Boolean jobStatus, Pageable pageable);

        Optional<Job> findByIdAndJobStatus(Long jobId, Boolean jobStatus);

        @Query("SELECT new com.jobportal.jobportal_api.dto.response.ViewAllJobsResponseDTO(" +
                        "j.id, j.title, j.description, j.location, " +
                        "j.companyName, j.jobStatus, j.salary, j.jobType, " +
                        "COUNT(a), j.createdTime) " +
                        "FROM Job j " +
                        "LEFT JOIN Application a ON a.jobs.id = j.id " +
                        "WHERE j.user.id = :employerId " +
                        "GROUP BY j.id, j.title, j.description, j.location, " +
                        "j.companyName, j.jobStatus, j.salary, j.jobType, j.createdTime")
        Page<ViewAllJobsResponseDTO> findJobsWithApplicationCount(Long employerId,
                        Pageable pageable);

        @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.application a LEFT JOIN FETCH a.user WHERE j.user.id = :userId")
        List<Job> findJobsWithApplicationsAndApplicantsByUserId(Long userId);

        @Query("SELECT j FROM Job j WHERE " +
                        "(:location IS NULL OR j.location = :location) AND " +
                        "(:jobType IS NULL OR j.jobType = :jobType) AND " +
                        "(:companyName IS NULL OR j.companyName = :companyName) AND " +
                        "(:minSalary IS NULL OR j.salary >= :minSalary) AND " +
                        "(:maxSalary IS NULL OR j.salary <= :maxSalary)")
        List<Job> filterJobs(
                        @Param("location") String location,
                        @Param("jobType") String jobType,
                        @Param("companyName") String companyName,
                        @Param("minSalary") Double minSalary,
                        @Param("maxSalary") Double maxSalary);

        Optional<Job> findByIdAndUser_Id(Long jobId, Long userId);

}
