package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.UserProfileDetailsAdminResponseDTO;
import com.jobportal.jobportal_api.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileDetailsAdminMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdTime", source = "createdTime")
    UserProfileDetailsAdminResponseDTO toUserProfileDetailsAdminResponseDTO(User user);
}
