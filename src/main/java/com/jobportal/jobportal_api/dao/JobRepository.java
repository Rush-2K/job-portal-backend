package com.jobportal.jobportal_api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jobportal.jobportal_api.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

        boolean existsByIdAndUser_Id(Long jobId, Long userId);

        List<Job> findByJobStatus(Boolean jobStatus);

        Optional<Job> findByIdAndJobStatus(Long jobId, Boolean jobStatus);

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

}
