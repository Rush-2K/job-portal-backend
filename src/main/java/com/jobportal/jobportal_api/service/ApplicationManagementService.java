package com.jobportal.jobportal_api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.ApplicationRepository;
import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dto.response.ApplicationDetailResponseDTO;
import com.jobportal.jobportal_api.dto.response.ApplicationSummaryResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.mapper.ApplicationDetailMapper;
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
    private ApplicationDetailMapper applicationDetailMapper;

    public ApplicationManagementService(ApplicationRepository applicationRepository, UserService userService,
            JobRepository jobRepository, ApplicationSummaryMapper applicationSummaryMapper,
            ApplicationDetailMapper applicationDetailMapper) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
        this.jobRepository = jobRepository;
        this.applicationSummaryMapper = applicationSummaryMapper;
        this.applicationDetailMapper = applicationDetailMapper;
    }

    @Transactional
    public List<ApplicationSummaryResponseDTO> viewAllApplications() {
        // get userid inside token
        Long userId = userService.getUserIdInsideToken();

        // verify user with Job entity
        List<Job> listOfJobs = jobRepository.findJobsWithApplicationsAndApplicantsByUserId(userId);
        for (Job job : listOfJobs) {
            log.info("Job {} has {} applications", job.getTitle(),
                    job.getApplication().size());
        }

        // return to DTO
        return applicationSummaryMapper.toApplicationSummaryResponseDTOs(listOfJobs);

    }

    public ApplicationDetailResponseDTO getApplicationDetails(Long applicationId) {

        Application applicationDetails = applicationRepository.findApplicationDetailsWithId(applicationId);

        return applicationDetailMapper.toApplicationDetailResponseDTO(applicationDetails);
    }

}
