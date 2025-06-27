package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.UpdateApplicationStatusResponseDTO;
import com.jobportal.jobportal_api.entity.Application;
import com.jobportal.jobportal_api.enums.ApplicationStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UpdateApplicationStatusMapper {

    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "previousStatus", source = "prevStatus")
    @Mapping(target = "newStatus", source = "application.status")
    @Mapping(target = "updatedTime", source = "application.updatedTime")
    UpdateApplicationStatusResponseDTO toUpdateApplicationStatusResponseDTO(Application application,
            ApplicationStatus prevStatus);
}
