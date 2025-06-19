package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jobportal.jobportal_api.dto.request.UpdateUserProfileRequestDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;
import com.jobportal.jobportal_api.entity.User;

@Mapper(componentModel = "spring")
public interface UpdateProfileMapper {

    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    UserProfileResponseDTO toUserProfileResponseDTO(User user);
}
