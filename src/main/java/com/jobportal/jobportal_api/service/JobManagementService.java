package com.jobportal.jobportal_api.service;

import java.lang.classfile.ClassFile.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.request.CreateJobRequestDTO;
import com.jobportal.jobportal_api.dto.response.CreateJobResponseDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;
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

    public List<CreateJobResponseDTO> createJobPost(List<CreateJobRequestDTO> createJobRequestDTO) {
        // get the currently authenticated user
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        String userId = userProfileResponseDTO.getUserId();
        log.info(": {}", userId);

        // retrieve the user entity from DB
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ;

        List<Job> jobsToSave = createJobRequestDTO.stream().map(dto -> {
            // create job post
            Job job = new Job();
            job.setTitle(dto.getTitle());
            job.setDescription(dto.getDescription());
            job.setLocation(dto.getLocation());
            job.setCompanyName(dto.getCompanyName());
            job.setSalary(dto.getSalary());
            job.setJobType(dto.getJobType());
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

    public List<ViewAllJobsResponseDTO> getAllJobs() {
        List<Job> allJobs = jobRepository.findAll();

        return allJobs.stream()
                .map(viewAllJobsMapper::toViewAllJobsResponseDTO)
                .collect(Collectors.toList());

    }

    public void deleteJobPostById(Long jobId) {
        // check if the user is valid/token is valid
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        String tokenUserId = userProfileResponseDTO.getUserId();

        // get the user by userId
        User userOpt = userRepository.findByUserId(tokenUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // get the id
        Long realId = userOpt.getId();
        log.info("Job Id: {}, Real UserId: {}", jobId, realId);

        // cross check the user id with the posted by id
        boolean check = jobRepository.existsByIdAndUser_Id(jobId, realId);
        log.info("Check: {}", check);

        if (!check) {
            throw new AccessDeniedException("You are not authorized to delete");
        }

        // delete if ok
        Job jobToBeDelete = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job Not found"));
        jobRepository.delete(jobToBeDelete);
    }

}
