package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.ViewActiveJobDetailsResponseDTO;
import com.jobportal.jobportal_api.entity.Job;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ViewActiveJobDetailsMapper {

    @Mapping(target = "title", source = "jobs.title")
    @Mapping(target = "description", source = "jobs.description")
    @Mapping(target = "location", source = "jobs.location")
    @Mapping(target = "companyName", source = "jobs.companyName")
    @Mapping(target = "salary", source = "jobs.salary")
    @Mapping(target = "jobType", source = "jobs.jobType")
    @Mapping(target = "createdTime", source = "jobs.createdTime")
    ViewActiveJobDetailsResponseDTO toViewActiveJobDetailsResponseDTO(Job jobs);
}
