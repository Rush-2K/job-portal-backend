package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

import com.jobportal.jobportal_api.enums.ApplicationStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateApplicationStatusResponseDTO {
    private Long applicationId;
    private ApplicationStatus previousStatus;
    private ApplicationStatus newStatus;
    private LocalDateTime updatedTime;
}
