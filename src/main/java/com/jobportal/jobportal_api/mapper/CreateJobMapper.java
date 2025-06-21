package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.CreateJobResponseDTO;
import com.jobportal.jobportal_api.entity.Job;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreateJobMapper {

    @Mapping(target = "id", source = "job.id")
    @Mapping(target = "jobTitle", source = "job.title")
    @Mapping(target = "name", source = "job.user.name")
    CreateJobResponseDTO toCreateJobResponseDTO(Job job);
}
