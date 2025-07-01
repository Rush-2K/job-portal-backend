package com.jobportal.jobportal_api.dto.request;

import com.jobportal.jobportal_api.enums.UserStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserStatusRequestDTO {
    private Long userId;
    private UserStatus status;
}
