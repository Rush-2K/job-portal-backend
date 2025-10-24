package com.jobportal.jobportal_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.request.CreateJobRequestDTO;
import com.jobportal.jobportal_api.dto.request.UpdateJobDetailsRequestDTO;
import com.jobportal.jobportal_api.dto.request.UpdateJobStatusRequestDTO;
import com.jobportal.jobportal_api.dto.response.CreateJobResponseDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewActiveJobDetailsResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewAllJobsResponseDTO;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.mapper.CreateJobMapper;
import com.jobportal.jobportal_api.mapper.ViewAllJobsMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobManagementService {

    private JobRepository jobRepository;
    private UserRepository userRepository;
    private UserService userService;
    private CreateJobMapper createJobMapper;
    private ViewAllJobsMapper viewAllJobsMapper;

    public JobManagementService(JobRepository jobRepository, UserRepository userRepository, UserService userService,
            CreateJobMapper createJobMapper, ViewAllJobsMapper viewAllJobsMapper) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.createJobMapper = createJobMapper;
        this.viewAllJobsMapper = viewAllJobsMapper;
    }

    public CreateJobResponseDTO createJobPost(CreateJobRequestDTO createJobRequestDTO) {
        // get the currently authenticated user
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        Long userId = userProfileResponseDTO.getUserId();
        log.info(": {}", userId);

        // retrieve the user entity from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job jobsToSave = new Job();
        jobsToSave.setTitle(createJobRequestDTO.getTitle());
        jobsToSave.setDescription(createJobRequestDTO.getDescription());
        jobsToSave.setLocation(createJobRequestDTO.getLocation());
        jobsToSave.setCompanyName(createJobRequestDTO.getCompanyName());
        jobsToSave.setSalary(createJobRequestDTO.getSalary());
        jobsToSave.setJobType(createJobRequestDTO.getJobType());
        jobsToSave.setJobStatus(true);
        jobsToSave.setCreatedTime(LocalDateTime.now());
        jobsToSave.setUpdatedTime(LocalDateTime.now());

        jobsToSave.setUser(user);

        // save job
        Job savedJob = jobRepository.save(jobsToSave);

        return createJobMapper.toCreateJobResponseDTO(savedJob);

    }

    public List<CreateJobResponseDTO> createMultipleJobPost(List<CreateJobRequestDTO> createJobRequestDTO) {
        // get the currently authenticated user
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        Long userId = userProfileResponseDTO.getUserId();
        log.info(": {}", userId);

        // retrieve the user entity from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Job> jobsToSave = createJobRequestDTO.stream().map(dto -> {
            // create job post
            Job job = new Job();
            job.setTitle(dto.getTitle());
            job.setDescription(dto.getDescription());
            job.setLocation(dto.getLocation());
            job.setCompanyName(dto.getCompanyName());
            job.setSalary(dto.getSalary());
            job.setJobType(dto.getJobType());
            job.setJobStatus(true);
            job.setCreatedTime(LocalDateTime.now());
            job.setUpdatedTime(LocalDateTime.now());

            // relate job to user
            job.setUser(user);

            return job;
        }).collect(Collectors.toList());

        // save job
        List<Job> savedJobs = jobRepository.saveAll(jobsToSave);

        // return with mapper
        return savedJobs.stream()
                .map(createJobMapper::toCreateJobResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<ViewAllJobsResponseDTO> getAllJobs(Pageable pageable) {
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        Page<ViewAllJobsResponseDTO> allJobs = jobRepository
                .findJobsWithApplicationCount(userProfileResponseDTO.getUserId(), pageable);

        return allJobs;
    }

    public boolean verifyPostedByWithUserId(Long jobId) {
        // check if the user is valid/token is valid
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        Long tokenUserId = userProfileResponseDTO.getUserId();

        // get the user by userId
        User userOpt = userRepository.findById(tokenUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // get the id
        Long realId = userOpt.getId();
        log.info("Job Id: {}, Real UserId: {}", jobId, realId);

        // cross check the user id with the posted by id
        boolean check = jobRepository.existsByIdAndUser_Id(jobId, realId);

        return check;
    }

    public void deleteJobPostById(Long jobId) {
        boolean check = verifyPostedByWithUserId(jobId);
        log.info("Check: {}", check);

        if (!check) {
            throw new AccessDeniedException("You are not authorized to delete");
        }

        // delete if ok
        Job jobToBeDelete = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job Not found"));
        jobRepository.delete(jobToBeDelete);
    }

    public void updateJobDetails(Long jobId, UpdateJobDetailsRequestDTO updateJobDetailsRequestDTO) {

        boolean check = verifyPostedByWithUserId(jobId);

        if (!check) {
            throw new AccessDeniedException("You are not authorized to update job details");
        }

        // get the job object using id
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job cannot be found"));

        // validate each field
        if (updateJobDetailsRequestDTO.getTitle() != null) {
            job.setTitle(updateJobDetailsRequestDTO.getTitle());
        }
        if (updateJobDetailsRequestDTO.getDescription() != null) {
            job.setDescription(updateJobDetailsRequestDTO.getDescription());
        }
        if (updateJobDetailsRequestDTO.getLocation() != null) {
            job.setLocation(updateJobDetailsRequestDTO.getLocation());
        }
        if (updateJobDetailsRequestDTO.getCompanyName() != null) {
            job.setCompanyName(updateJobDetailsRequestDTO.getCompanyName());
        }
        if (updateJobDetailsRequestDTO.getSalary() != null) {
            job.setSalary(updateJobDetailsRequestDTO.getSalary());
        }
        if (updateJobDetailsRequestDTO.getJobType() != null) {
            job.setJobType(updateJobDetailsRequestDTO.getJobType());
        }
        if (updateJobDetailsRequestDTO.getJobStatus() != null) {
            job.setJobStatus(updateJobDetailsRequestDTO.getJobStatus());
        }

        // set the updated time
        job.setUpdatedTime(LocalDateTime.now());

        // save
        jobRepository.save(job);
    }

    public void updateJobStatus(Long jobId, UpdateJobStatusRequestDTO updateJobStatusRequestDTO) {
        boolean check = verifyPostedByWithUserId(jobId);

        if (!check) {
            throw new AccessDeniedException("You are not authorized to update job status");
        }

        // get the job object using id
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job cannot be found"));

        if (updateJobStatusRequestDTO.getJobStatus() != null) {
            job.setJobStatus(updateJobStatusRequestDTO.getJobStatus());
        }

        // set the updated time
        job.setUpdatedTime(LocalDateTime.now());

        // save
        jobRepository.save(job);
    }

    public ViewAllJobsResponseDTO getJobPostedDetails(Long jobId) {
        // get userid
        Long tokenUserId = userService.getUserIdInsideToken();
        // cross check the table with the userid and jobid
        Job job = jobRepository.findByIdAndUser_Id(jobId, tokenUserId)
                .orElseThrow(() -> new EntityNotFoundException("Error occur.."));
        // return back
        return viewAllJobsMapper.toViewAllJobsResponseDTO(job);
    }

}
