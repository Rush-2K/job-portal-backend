package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;
import com.jobportal.jobportal_api.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDetailsMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "createdAt", source = "user.createdTime")
    UserProfileResponseDTO toUserProfileResponseDTO(User user);
}
