package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobportal.jobportal_api.dto.response.JobApplicationResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.JobApplicationService;

@RestController
@RequestMapping("/api/jobapplication")
public class JobApplicationController {

    private JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/{jobId}/apply")
    public ResponseEntity<?> applyToJob(
            @PathVariable Long jobId,
            @RequestPart(value = "resume", required = false) MultipartFile resumeFile) {
        JobApplicationResponseDTO jobApplication = jobApplicationService.applyJob(jobId, resumeFile);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), jobApplication));
    }

}
