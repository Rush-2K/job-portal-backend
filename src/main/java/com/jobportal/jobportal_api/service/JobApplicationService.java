package com.jobportal.jobportal_api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jobportal.jobportal_api.dao.ApplicationRepository;
import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.response.JobApplicationResponseDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.enums.ApplicationStatus;
import com.jobportal.jobportal_api.mapper.JobApplicationMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobApplicationService {

    private JobRepository jobRepository;
    private UserRepository userRepository;
    private ApplicationRepository applicationRepository;
    private UserService userService;
    private S3Service s3Service;
    private JobApplicationMapper jobApplicationMapper;

    public JobApplicationService(JobRepository jobRepository, UserRepository userRepository,
            ApplicationRepository applicationRepository, UserService userService, S3Service s3Service,
            JobApplicationMapper jobApplicationMapper) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.userService = userService;
        this.s3Service = s3Service;
        this.jobApplicationMapper = jobApplicationMapper;
    }

    public JobApplicationResponseDTO applyJob(Long jobId, MultipartFile resume) {
        // verify user
        // check if the user is valid/token is valid
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        String tokenUserId = userProfileResponseDTO.getUserId();

        // get the user by userId
        User userOpt = userRepository.findByUserId(tokenUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // get job name
        Job jobApplied = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job Not Found"));

        boolean applicationCheck = applicationRepository.existsByJobs_IdAndUser_Id(jobId, userOpt.getId());

        // check if user already applied for the job
        if (applicationCheck) {
            throw new IllegalStateException("User has already applied for this job");
        }

        // upload resume to s3
        String resumeUrl = s3Service.upload(resume, userOpt.getUserId());

        // create new application
        Application newApplication = new Application();

        newApplication.setUser(userOpt);
        newApplication.setJobs(jobApplied);
        newApplication.setResumeUrl(resumeUrl);
        newApplication.setStatus(ApplicationStatus.PENDING);
        newApplication.setAppliedTime(LocalDateTime.now());

        // save
        applicationRepository.save(newApplication);
        return jobApplicationMapper.toJobApplicationResponseDTO(newApplication, jobApplied, userOpt);

    }

}
