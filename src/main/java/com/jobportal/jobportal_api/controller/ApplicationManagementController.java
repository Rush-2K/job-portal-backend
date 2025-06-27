package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.response.ApplicationDetailResponseDTO;
import com.jobportal.jobportal_api.dto.response.ApplicationSummaryResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.ApplicationManagementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/applicationmgmt")
public class ApplicationManagementController {

    private ApplicationManagementService applicationManagementService;

    public ApplicationManagementController(ApplicationManagementService applicationManagementService) {
        this.applicationManagementService = applicationManagementService;
    }

    @GetMapping("/viewAllApplication")
    public ResponseEntity<?> viewAllApplications() {
        log.info("view all application controller started");
        List<ApplicationSummaryResponseDTO> responses = applicationManagementService.viewAllApplications();
        log.info("List of jobs: {}", responses.stream()
                .map(response -> response.getJobTitle() + " (" + response.getApplicantName() + ")")
                .collect(Collectors.joining(", ")));
        log.info("view all application controller ended");
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), responses));
    }

    @GetMapping("/viewdetails/{appId}")
    public ResponseEntity<?> viewDetails(@PathVariable Long appId) {
        ApplicationDetailResponseDTO response = applicationManagementService.getApplicationDetails(appId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), response));
    }

}
