package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.response.ViewActiveJobDetailsResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewAllActiveJobsResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.JobBrowseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/jobs")
public class JobBrowseController {

    private JobBrowseService jobBrowseService;

    public JobBrowseController(JobBrowseService jobBrowseService) {
        this.jobBrowseService = jobBrowseService;
    }

    // view all active jobs only
    @GetMapping("/view")
    public ResponseEntity<?> listAllActiveJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ViewAllActiveJobsResponseDTO> data = jobBrowseService.getAllActiveJobs(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), data));
    }

    // view all active jobs only
    @GetMapping("/view/{jobId}")
    public ResponseEntity<?> viewJobDetails(@PathVariable Long jobId) {
        ViewActiveJobDetailsResponseDTO data = jobBrowseService.getActiveJobDetails(jobId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), data));
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary) {
        List<Job> filtered = jobBrowseService.filterJobs(location, jobType, companyName, minSalary, maxSalary);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), filtered));
    }

}
