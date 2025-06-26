package com.jobportal.jobportal_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jobportal.jobportal_api.dao.ApplicationRepository;
import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.response.JobApplicationResponseDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewOwnApplicationsResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.enums.ApplicationStatus;
import com.jobportal.jobportal_api.mapper.JobApplicationMapper;
import com.jobportal.jobportal_api.mapper.ViewOwnApplicationsMapper;

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
    private ViewOwnApplicationsMapper viewOwnApplicationsMapper;

    public JobApplicationService(JobRepository jobRepository, UserRepository userRepository,
            ApplicationRepository applicationRepository, UserService userService, S3Service s3Service,
            JobApplicationMapper jobApplicationMapper, ViewOwnApplicationsMapper viewOwnApplicationsMapper) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.userService = userService;
        this.s3Service = s3Service;
        this.jobApplicationMapper = jobApplicationMapper;
        this.viewOwnApplicationsMapper = viewOwnApplicationsMapper;
    }

    public JobApplicationResponseDTO applyJob(Long jobId, MultipartFile resume) {
        // verify user
        // check if the user is valid/token is valid
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        Long tokenUserId = userProfileResponseDTO.getUserId();

        // get the user by userId
        User userOpt = userRepository.findById(tokenUserId)
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
        newApplication.setUpdatedTime(LocalDateTime.now());

        // save
        applicationRepository.save(newApplication);
        return jobApplicationMapper.toJobApplicationResponseDTO(newApplication, jobApplied, userOpt);

    }

    public List<ViewOwnApplicationsResponseDTO> viewJob() {
        log.info("View Jobs INPRG");
        // get user id from token
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        Long tokenUserId = userProfileResponseDTO.getUserId();
        log.info("User Id: ", tokenUserId);
        // get the application based on user_id
        List<Application> appliedApplications = applicationRepository.findByUser_Id(tokenUserId);

        log.info("List of applications: {}", appliedApplications);
        // get the job based on job_id that get from application
        // List<Job> appliedJobs = appliedApplications.stream()
        // .map(Application::getJobs)
        // .collect(Collectors.toList());

        // return
        return viewOwnApplicationsMapper.toViewOwnApplicationsResponseDTOList(appliedApplications);
    }

    public void withdrawJob(Long applicationId) {
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        Long tokenUserId = userProfileResponseDTO.getUserId();
        Application appliedJob = applicationRepository.findByIdAndUser_Id(applicationId, tokenUserId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Application Not Found or does not belong to current user"));

        appliedJob.setStatus(ApplicationStatus.WITHDRAWN);
        appliedJob.setUpdatedTime(LocalDateTime.now());

        applicationRepository.save(appliedJob);
    }

}
