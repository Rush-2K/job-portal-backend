package com.jobportal.jobportal_api.dto.response;

import com.jobportal.jobportal_api.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserStatusResponseDTO {
    private Long userId;
    private UserStatus status;
}
