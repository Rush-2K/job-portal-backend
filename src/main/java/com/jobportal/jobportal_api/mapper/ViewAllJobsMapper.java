package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.jobportal.jobportal_api.dto.response.ViewAllJobsResponseDTO;
import com.jobportal.jobportal_api.entity.Job;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ViewAllJobsMapper {

    @Mapping(target = "id", source = "job.id")
    @Mapping(target = "title", source = "job.title")
    @Mapping(target = "description", source = "job.description")
    @Mapping(target = "location", source = "job.location")
    @Mapping(target = "companyName", source = "job.companyName")
    @Mapping(target = "salary", source = "job.salary")
    @Mapping(target = "jobType", source = "job.jobType")
    @Mapping(target = "jobStatus", source = "job.jobStatus")
    @Mapping(target = "createdTime", source = "job.createdTime")
    ViewAllJobsResponseDTO toViewAllJobsResponseDTO(Job job);
}
