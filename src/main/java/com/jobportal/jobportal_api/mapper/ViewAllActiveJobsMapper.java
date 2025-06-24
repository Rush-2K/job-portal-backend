package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.ViewAllActiveJobsResponseDTO;
import com.jobportal.jobportal_api.entity.Job;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ViewAllActiveJobsMapper {

    @Mapping(target = "title", source = "jobs.title")
    @Mapping(target = "location", source = "jobs.location")
    @Mapping(target = "createdTime", source = "jobs.createdTime")
    ViewAllActiveJobsResponseDTO toViewAllActiveJobsResponseDTO(Job jobs);
}
