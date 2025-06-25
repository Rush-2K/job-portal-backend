package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.JobApplicationResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobApplicationMapper {

    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "jobTitle", source = "job.title")
    @Mapping(target = "resumeUrl", source = "application.resumeUrl")
    @Mapping(target = "appliedTime", source = "application.appliedTime")
    JobApplicationResponseDTO toJobApplicationResponseDTO(Application application, Job job, User user);
}
