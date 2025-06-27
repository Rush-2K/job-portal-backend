package com.jobportal.jobportal_api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.ApplicationRepository;
import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dto.response.ApplicationSummaryResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.mapper.ApplicationSummaryMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationManagementService {

    private ApplicationRepository applicationRepository;
    private UserService userService;
    private JobRepository jobRepository;
    private ApplicationSummaryMapper applicationSummaryMapper;

    public ApplicationManagementService(ApplicationRepository applicationRepository, UserService userService,
            JobRepository jobRepository, ApplicationSummaryMapper applicationSummaryMapper) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
        this.jobRepository = jobRepository;
        this.applicationSummaryMapper = applicationSummaryMapper;
    }

    @Transactional
    public List<ApplicationSummaryResponseDTO> viewAllApplications() {
        log.info("view all application service started");
        // get userid inside token
        Long userId = userService.getUserIdInsideToken();
        log.info("userId: {}", userId);

        // verify user with Job entity
        List<Job> listOfJobs = jobRepository.findJobsWithApplicationsAndApplicantsByUserId(userId);
        for (Job job : listOfJobs) {
            log.info("Job {} has {} applications", job.getTitle(),
                    job.getApplication().size());
        }

        log.info("verify user job entity done");

        log.info("view all application service ended");

        // return to DTO
        return applicationSummaryMapper.toApplicationSummaryResponseDTOs(listOfJobs);

    }

}
