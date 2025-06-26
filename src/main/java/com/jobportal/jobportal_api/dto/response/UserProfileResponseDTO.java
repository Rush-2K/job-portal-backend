package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponseDTO {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
