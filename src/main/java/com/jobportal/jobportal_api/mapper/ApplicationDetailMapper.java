package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.ApplicationDetailResponseDTO;
import com.jobportal.jobportal_api.entity.Application;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationDetailMapper {

    @Mapping(target = "applicationId", source = "id")
    @Mapping(target = "jobId", source = "jobs.id")
    @Mapping(target = "applicantName", source = "user.name")
    @Mapping(target = "applicantEmail", source = "user.email")
    @Mapping(target = "jobTitle", source = "jobs.title")
    @Mapping(target = "jobType", source = "jobs.jobType")
    @Mapping(target = "salary", source = "jobs.salary")
    @Mapping(target = "resumeLink", source = "resumeUrl")
    @Mapping(target = "appliedDate", expression = "java(application.getAppliedTime().toLocalDate())")
    @Mapping(target = "applicationStatus", expression = "java(application.getStatus().name())")
    ApplicationDetailResponseDTO toApplicationDetailResponseDTO(Application application);
}
