package com.jobportal.jobportal_api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jobportal.jobportal_api.dto.response.ViewOwnApplicationsResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.entity.Job;

@Component
public class ViewOwnApplicationsMapper {
    public List<ViewOwnApplicationsResponseDTO> toViewOwnApplicationsResponseDTOList(List<Application> applications) {
        return applications.stream().map(application -> {
            Job job = application.getJobs();

            ViewOwnApplicationsResponseDTO dto = new ViewOwnApplicationsResponseDTO();
            dto.setJobTitle(job.getTitle());
            dto.setCompanyName(job.getCompanyName());
            dto.setJobLocation(job.getLocation());

            dto.setApplicationStatus(application.getStatus().name());
            dto.setDateApplied(application.getAppliedTime().toLocalDate());
            dto.setResumeLink(application.getResumeUrl());

            return dto;
        }).collect(Collectors.toList());
    }

}
