package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.request.CreateJobRequestDTO;
import com.jobportal.jobportal_api.dto.request.UpdateJobDetailsRequestDTO;
import com.jobportal.jobportal_api.dto.request.UpdateJobStatusRequestDTO;
import com.jobportal.jobportal_api.dto.response.CreateJobResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewAllJobsResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.JobManagementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/jobmgmt")
public class JobManagementController {

    private JobManagementService jobManagementService;

    public JobManagementController(JobManagementService jobManagementService) {
        this.jobManagementService = jobManagementService;
    }

    // view all jobs posted by employer, can see theirs only
    @GetMapping("/viewjobs/{userId}")
    public ResponseEntity<?> getAllJobs(@PathVariable Long userId) {
        List<ViewAllJobsResponseDTO> viewAllJobsResponses = jobManagementService.getAllJobs(userId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), viewAllJobsResponses));
    }

    @PostMapping("/createmultipost")
    public ResponseEntity<?> createMultiplePost(@RequestBody List<CreateJobRequestDTO> createJobRequestDTO) {
        List<CreateJobResponseDTO> responses = jobManagementService.createMultipleJobPost(createJobRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), responses));
    }

    @PostMapping("/createpost")
    public ResponseEntity<?> createPost(@RequestBody CreateJobRequestDTO createJobRequestDTO) {
        CreateJobResponseDTO responses = jobManagementService.createJobPost(createJobRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), responses));
    }

    // delete one job only
    @DeleteMapping("/deletejobpost/{jobId}")
    public ResponseEntity<?> deleteJobPost(@PathVariable Long jobId) {
        log.info("Delete job post hit");
        jobManagementService.deleteJobPostById(jobId);
        log.info("Delete job successfully");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), null));
    }

    // update job post details
    @PatchMapping("/updatejobpost/{jobId}")
    public ResponseEntity<?> updateJobDetails(@PathVariable Long jobId,
            @RequestBody UpdateJobDetailsRequestDTO updateJobDetailsRequestDTO) {
        jobManagementService.updateJobDetails(jobId, updateJobDetailsRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                "Job details updated successfully", LocalDateTime.now(), null));
    }

    // enable and disable visibility
    @PatchMapping("/updatejobstatus/{jobId}")
    public ResponseEntity<?> updateJobStatus(@PathVariable Long jobId,
            @RequestBody UpdateJobStatusRequestDTO updateJobStatusRequestDTO) {
        jobManagementService.updateJobStatus(jobId, updateJobStatusRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                "Job Status updated successfully", LocalDateTime.now(), null));
    }
}
