package com.jobportal.jobportal_api.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.ApplicationRepository;
import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dto.request.UpdateApplicationStatusRequestDTO;
import com.jobportal.jobportal_api.dto.response.ApplicationDetailResponseDTO;
import com.jobportal.jobportal_api.dto.response.ApplicationSummaryResponseDTO;
import com.jobportal.jobportal_api.dto.response.UpdateApplicationStatusResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.enums.ApplicationStatus;
import com.jobportal.jobportal_api.mapper.ApplicationDetailMapper;
import com.jobportal.jobportal_api.mapper.ApplicationSummaryMapper;
import com.jobportal.jobportal_api.mapper.UpdateApplicationStatusMapper;

import jakarta.persistence.EntityNotFoundException;
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
    private UpdateApplicationStatusMapper updateApplicationStatusMapper;

    public ApplicationManagementService(ApplicationRepository applicationRepository, UserService userService,
            JobRepository jobRepository, ApplicationSummaryMapper applicationSummaryMapper,
            ApplicationDetailMapper applicationDetailMapper,
            UpdateApplicationStatusMapper updateApplicationStatusMapper) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
        this.jobRepository = jobRepository;
        this.applicationSummaryMapper = applicationSummaryMapper;
        this.applicationDetailMapper = applicationDetailMapper;
        this.updateApplicationStatusMapper = updateApplicationStatusMapper;
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

    public UpdateApplicationStatusResponseDTO updateStatus(UpdateApplicationStatusRequestDTO request) {
        // check whether this job is belong to this employer
        Long tokenUserId = userService.getUserIdInsideToken();
        boolean checkJob = jobRepository.existsByIdAndUser_Id(request.getJobId(), tokenUserId);

        if (!checkJob) {
            throw new RuntimeException("You are not allowed to change the status");
        }
        // get application
        Application updateApplication = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new EntityNotFoundException("Application Not Found"));

        // check if application already withdraw
        if (updateApplication.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new RuntimeException("Application has already withdraw");
        }

        // get the previous status
        ApplicationStatus prevStatus = updateApplication.getStatus();

        // set
        updateApplication.setStatus(request.getStatus());
        updateApplication.setUpdatedTime(LocalDateTime.now());

        // save
        applicationRepository.save(updateApplication);

        // return
        return updateApplicationStatusMapper.toUpdateApplicationStatusResponseDTO(updateApplication, prevStatus);

    }

}
