package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobApplicationResponseDTO {

    private Long applicationId;
    private String name;
    private String jobTitle;
    private String resumeUrl;
    private LocalDateTime appliedTime;
}
