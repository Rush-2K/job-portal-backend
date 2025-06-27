package com.jobportal.jobportal_api.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jobportal.jobportal_api.dao.ApplicationRepository;
import com.jobportal.jobportal_api.dto.response.ApplicationSummaryResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;

@Component
public class ApplicationSummaryMapper {

    private final ApplicationRepository applicationRepository;

    public ApplicationSummaryMapper(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<ApplicationSummaryResponseDTO> toApplicationSummaryResponseDTOs(List<Job> listOfJobs) {
        return listOfJobs.stream()
                .flatMap(job -> {
                    // Fetch applications directly from the DB
                    List<Application> apps = applicationRepository.findByJobs_Id(job.getId());

                    return apps.stream().map(application -> {
                        User applicant = application.getUser();

                        ApplicationSummaryResponseDTO dto = new ApplicationSummaryResponseDTO();
                        dto.setApplicationId(application.getId());
                        dto.setJobId(job.getId());
                        dto.setJobTitle(job.getTitle());
                        dto.setApplicantName(applicant.getName());
                        dto.setApplicationStatus(application.getStatus());
                        dto.setAppliedDate(application.getAppliedTime().toLocalDate());
                        return dto;
                    });
                })
                .collect(Collectors.toList());
    }
}
