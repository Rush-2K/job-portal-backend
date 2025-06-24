package com.jobportal.jobportal_api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dto.response.ViewActiveJobDetailsResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewAllActiveJobsResponseDTO;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.mapper.ViewActiveJobDetailsMapper;
import com.jobportal.jobportal_api.mapper.ViewAllActiveJobsMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class JobBrowseService {

    private JobRepository jobRepository;
    private ViewAllActiveJobsMapper viewAllActiveJobsMapper;
    private ViewActiveJobDetailsMapper viewActiveJobDetailsMapper;

    public JobBrowseService(JobRepository jobRepository, ViewAllActiveJobsMapper viewAllActiveJobsMapper,
            ViewActiveJobDetailsMapper viewActiveJobDetailsMapper) {
        this.jobRepository = jobRepository;
        this.viewAllActiveJobsMapper = viewAllActiveJobsMapper;
        this.viewActiveJobDetailsMapper = viewActiveJobDetailsMapper;
    }

    public List<ViewAllActiveJobsResponseDTO> getAllActiveJobs() {
        List<Job> jobs = jobRepository.findByJobStatus(true);

        return jobs.stream()
                .map(viewAllActiveJobsMapper::toViewAllActiveJobsResponseDTO)
                .collect(Collectors.toList());

    }

    public ViewActiveJobDetailsResponseDTO getActiveJobDetails(Long jobId) {
        Job jobs = jobRepository.findByIdAndJobStatus(jobId, true)
                .orElseThrow(() -> new EntityNotFoundException("Job Not Found"));

        return viewActiveJobDetailsMapper.toViewActiveJobDetailsResponseDTO(jobs);

    }

    public List<Job> filterJobs(String location, String jobType, String companyName, Double minSalary,
            Double maxSalary) {
        List<Job> filtered = jobRepository.filterJobs(location, jobType, companyName, minSalary, maxSalary);

        return filtered;
    }

}
