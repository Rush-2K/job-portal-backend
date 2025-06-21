package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.request.CreateJobRequestDTO;
import com.jobportal.jobportal_api.dto.response.CreateJobResponseDTO;
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

    @PostMapping("/createpost")
    public ResponseEntity<?> createPost(@RequestBody List<CreateJobRequestDTO> createJobRequestDTO) {
        List<CreateJobResponseDTO> responses = jobManagementService.createJobPost(createJobRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), responses));
    }

}
