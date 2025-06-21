package com.jobportal.jobportal_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.request.CreateJobRequestDTO;
import com.jobportal.jobportal_api.dto.response.CreateJobResponseDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.mapper.CreateJobMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobManagementService {

    private JobRepository jobRepository;
    private UserRepository userRepository;
    private UserService userService;
    private CreateJobMapper createJobMapper;

    public JobManagementService(JobRepository jobRepository, UserRepository userRepository, UserService userService,
            CreateJobMapper createJobMapper) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.createJobMapper = createJobMapper;
    }

    public List<CreateJobResponseDTO> createJobPost(List<CreateJobRequestDTO> createJobRequestDTO) {
        // get the currently authenticated user
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        String userId = userProfileResponseDTO.getUserId();
        log.info(": {}", userId);

        // retrieve the user entity from DB
        User user = userRepository.findByUserId(userId);

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

}
