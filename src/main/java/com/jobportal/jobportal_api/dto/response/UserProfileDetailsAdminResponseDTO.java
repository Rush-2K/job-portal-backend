package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

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
public class UserProfileDetailsAdminResponseDTO {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private UserStatus status;
    private LocalDateTime createdTime;
    private String message;
}
